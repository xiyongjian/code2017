package gosigma.etl_log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.HandlerMapping;

@Controller
public class FileController {
	public static Logger log = LoggerFactory.getLogger(FileController.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String str = "e:/code/git/gosigma_etl/..";
		File file = new File(str);
		System.out.println("file is : " + file.getName());
		System.out.println("file is dir ? : " + file.isDirectory());
		System.out.println("list : \n" + String.join("\n", getFileList(str)));

		List<File> files = Arrays.asList(file.listFiles());
		for (File f : files)
			System.out.println("File name : " + f.getName());

	}

	public static List<String> getFileList(String baseDir) {
		List<String> list = new ArrayList<>();
		File base = new File(baseDir);
		if (base.isDirectory())
			list = Arrays.asList(base.list());

		return list;
	}

	public static void getList(String baseDir, List<String> dirs, List<String> files) {
		File base = new File(baseDir);
		List<File> list = Arrays.asList(base.listFiles());
		if (base.isDirectory())
			dirs.add("..");
		for (File f : list) {
			if (f.isDirectory())
				dirs.add(f.getName() + "/");
			else
				files.add(f.getName());
		}
	}

	@Value("${etl.dir}")
	private String _etlDir;

	// @RequestMapping("/etl/")
	@RequestMapping(value = "/etl/", method = RequestMethod.GET)
	public String get(Model model) {
		log.info("get /etl");
		return doGet("", model);

	}

	@RequestMapping(value = "/etl/**", method = RequestMethod.GET)
	public String get(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String target = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		target = target.substring(5);
		log.info("get /etl/" + target);
		String rsl = doGet(target, model);
		if (rsl.equals("file")) { // it's file
			log.info("get file : /etl_files/" + target);
			response.sendRedirect("/etl_files/" + target);
			return null;
		}
		return rsl;
	}

	public String doGet(String target, Model model) {
		log.info("target : " + target);

		String baseDir = _etlDir + File.separator + target;
		log.info("baseDir : " + baseDir);
		File base = new File(baseDir);
		if (!base.exists()) {
			model.addAttribute("errorMsg", baseDir + " not exists or not a directory");
			return "error";
		}
		if (!base.isDirectory()) {
			return "file";
		}

		List<String> dirs = new ArrayList<>();
		List<String> files = new ArrayList<>();
		getList(baseDir, dirs, files);

		log.info("base : " + base);
		log.info("dirs  list : " + String.join("\n", dirs));
		log.info("files list : " + String.join("\n", files));

		model.addAttribute("target", target);
		model.addAttribute("files", files);
		model.addAttribute("dirs", dirs);
		return "list";
	}

	@RequestMapping(value = "/get_file", method = RequestMethod.GET)
	public void getFile(@RequestParam(value = "file", defaultValue = "") String fileName, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		log.info("getFile, fileName : " + fileName);
		if ("".equals(fileName)) {
			response.sendRedirect("/etl/");
			return;
		}

		File file = new File(_etlDir + File.separator + fileName);
		log.info("get file : " + file.getAbsolutePath());
		// response.getWriter().println("get file : " + file.getAbsolutePath());
		
		// set file name
		// force download
		// response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
		// try browser open it
		response.setHeader("Content-Disposition", "inline; filename=" + file.getName());

		InputStream inputStream = new FileInputStream(file); // load the file
		IOUtils.copy(inputStream, response.getOutputStream());
		response.flushBuffer();
	}
}

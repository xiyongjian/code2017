package gosigma.study.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JacksonApp {
	public static void p(String msg) {
		System.out.println(msg);
	}

	public static void main(String[] args) throws JsonProcessingException, IOException {
		p("JacksonApp, testing jackson json api");
		test01();
		test02();
	}

	public static void test01() {
		p("\n--- test01");
	}

	public static void test02() throws JsonProcessingException, IOException {
		p("\n--- test02 node/tree/mapper...");
		ObjectMapper mapper = new ObjectMapper();

		{
			String str = "{\"nick\": \"cowtowncoder\"}";
			JsonNode node = mapper.readTree(str);
			p("str  : " + str);
			p("json : " + node.toString());
		}
		{
			String str = "{\r\n" + 
					"    \"response\": {\r\n" + 
					"        \"features\": {\r\n" + 
					"            \"history\": 1\r\n" + 
					"        }\r\n" + 
					"     },\r\n" + 
					"    \"history\": {\r\n" + 
					"        \"date\": {\r\n" + 
					"            \"pretty\": \"April 13, 2010\",\r\n" + 
					"            \"year\": \"2010\",\r\n" + 
					"            \"mon\": \"04\",\r\n" + 
					"            \"mday\": \"13\",\r\n" + 
					"            \"hour\": \"12\",\r\n" + 
					"            \"min\": \"00\",\r\n" + 
					"            \"tzname\": \"America/Los_Angeles\"\r\n" + 
					"        }\r\n" + 
					"    }\r\n" + 
					"}";
			JsonNode node = mapper.readTree(str);
			p("str  : " + str);
			p("json : " + node.toString());
			p("json 2 : " + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node));
			String path = "/response/features/history";
			p(path + ":" + node.at(path).intValue());
			p(path + ":" + node.at(path).asText());
			
			path = "/history/date/pretty";
			p(path + ":" + node.at(path).textValue());
			p(path + ":" + node.at(path).asText());

			path = "/history/date";
			p(path + ":" + node.at(path).textValue());
			p(path + ":" + node.at(path).asText());
			p(path + ":" + node.at(path).toString());
			p(path + " is missing :" + node.at(path).isMissingNode());

			path = "/history/datexxx";
			p(path + ":" + node.at(path).toString());
			p(path + " is null :" + node.at(path).isNull());
			p(path + " is missing :" + node.at(path).isMissingNode());
			
			ObjectNode on = (ObjectNode)node.at("/history");
			p("put datexxxx in /history");
			on.put("datexxx", "world");
			p(path + ":" + node.at(path).toString());
			p(path + " is null :" + node.at(path).isNull());
			p(path + " is missing :" + node.at(path).isMissingNode());
			
			node = mapper.createObjectNode();
			on = (ObjectNode)node;
			on.put("datexxx", "world");
			on.put("date", "world");
			p("new node : " + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node));
		}
	}

}

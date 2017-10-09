package gosigma.dns;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CheckCache {
   public static void main(String[] args) throws Exception {

      // put some values in the internal DNS cache

      // good DNS name
      InetAddress.getByName("stackoverflow.com");
      InetAddress.getByName("www.google.com");
      InetAddress.getByName("www.rgagnon.com");
      try {
         // bad DNS name
         InetAddress.getByName("bad.rgagnon.com");
      }
      catch (UnknownHostException e) {
         // do nothing
      }

      // dump the good DNS entries
      String addressCache = "addressCache";
      System.out.println("---------" + addressCache + "---------");
      printDNSCache(addressCache);

      // dump the bad DNS entries
      String negativeCache = "negativeCache";
      System.out.println("---------" + negativeCache + "---------");
      printDNSCache(negativeCache);
   }

   /**
    * By introspection, dump the InetAddress internal DNS cache
    *
    * @param cacheName  can be addressCache or negativeCache
    * @throws Exception
    */

   @SuppressWarnings({ "rawtypes", "unchecked" })
   private static void printDNSCache(String cacheName) throws Exception {
      Class<InetAddress> iaclass = InetAddress.class;
      Field acf = iaclass.getDeclaredField(cacheName);
      acf.setAccessible(true);
      Object addressCache = acf.get(null);
      Class cacheClass = addressCache.getClass();
      Field cf = cacheClass.getDeclaredField("cache");
      cf.setAccessible(true);
      Map<String, Object> cache = (Map<String, Object>) cf.get(addressCache);
      for (Map.Entry<String, Object> hi : cache.entrySet()) {
         Object cacheEntry = hi.getValue();
         Class cacheEntryClass = cacheEntry.getClass();
         Field expf = cacheEntryClass.getDeclaredField("expiration");
         expf.setAccessible(true);
         long expires = (Long) expf.get(cacheEntry);

         Field af = cacheEntryClass.getDeclaredField("addresses"); // JDK 1.7, older version maybe "address"
         af.setAccessible(true);
         InetAddress[] addresses = (InetAddress[]) af.get(cacheEntry);
         List<String> ads = new ArrayList<String>(addresses.length);

         for (InetAddress address : addresses) {
            ads.add(address.getHostAddress());
         }

         System.out.println(hi.getKey() + " "+new Date(expires) +" " +ads);
      }
   }
}
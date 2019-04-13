/**
 * @author Mursyid Afiq
 * Tuesday, November 20, 2018
 * Email: syid98@gmail.com
 */

package WhatsappStickerV1;

import static java.lang.System.out;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class jsoup {
	
	public static String trimStickerURL(String attribute) {
		String i = attribute.replace("background-image:url(","");
		i = i.replace(";compress=true);","");
		return i;
	}
	
	public static int getRangeID(String trimmedStickerURL) {
		String i = trimmedStickerURL.replace("https://stickershop.line-scdn.net/stickershop/v1/sticker/","");
		i = i.replace("/ANDROID/sticker.png","");	
		return Integer.parseInt(i);
	}
	
	public static String getStickerLogoID(String trimtrayIconURL) {
		String i = trimtrayIconURL.replace("https://stickershop.line-scdn.net/stickershop/v1/product/","");
		i = i.replace("/LINEStorePC/main.png","");
		return i;
	}
	
	public static Elements getStickerName(Document documentConnection) {
		Elements stickerName = documentConnection.getElementsByClass("mdCMN08Ttl");
		return stickerName;
	}
	
	public static Document connectJsoup(String sticker_url) throws IOException {
		Document doc = Jsoup.connect(sticker_url).get();
		return doc;
	}
	
	public static String getStickerURL(Document documentConnection, String mode) {
		String a=null;
		if(mode.equals("last")) {
			Element e = documentConnection.select("span[style]").last();
		    a = e.attr("style");
		} else if(mode.equals("first")) {
			Element e = documentConnection.select("span[style]").first();
			a = e.attr("style");
		}
		return a;
	}
	
	public static String getStickerLogoURL(Document documentConnection) {
		Element i = documentConnection.select("img[src]").first();
		String j = i.attr("src");
		return j;
	}
	
	public static String trimTrayIconURL(String attribute) {
		String i = attribute.replace(";compress=true?__=20161019","");
		return i;
	}
	
	
	public static String getLogoID(String trimmedStickerLogoURL) {
		String i = trimmedStickerLogoURL.replace("https://stickershop.line-scdn.net/stickershop/v1/product/","");
		i = i.replace("/LINEStorePC/main.png","");
		return i;
	}
	
	public static void main(String... reclean) throws IOException {
		String sticker_url = "https://store.line.me/stickershop/product/1445168/en";
		// connection
		Document documentConnection = connectJsoup(sticker_url);
		
		// get sticker title based on URL
		Elements title = getStickerName(documentConnection);
		out.println(title.text());
		
		String firstAttribute = getStickerURL(documentConnection, "first");
		// get first sticker URL
		out.println(trimStickerURL(firstAttribute));
		// get first sticker range ID
		out.println( getRangeID( trimStickerURL(firstAttribute)));

		String lastAttribute = getStickerURL(documentConnection, "last");
		// get last sticker URL
		out.println(trimStickerURL(lastAttribute));
		// get last sticker range ID
		out.println( getRangeID( trimStickerURL(lastAttribute)));

		String logoAttributes = getStickerLogoURL(documentConnection);
		// get sticker logo url
		out.println( trimTrayIconURL(logoAttributes));
		// get sticker logo ID
		out.println( getLogoID( trimTrayIconURL(logoAttributes)));
	}

}

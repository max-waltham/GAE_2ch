package jp.blogspot.simple_asta.b2.server;

import java.io.IOException;
import java.nio.charset.Charset;

import jp.blogspot.simple_asta.b2.client.GreetingService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	public byte[] greetServer(String input) throws IllegalArgumentException {
		input = escapeHtml(input);
		Charset utf8 = Charset.forName("UTF-8");

		if (input.equals("CategoryList")) {
			String[][][] cl = B2.getCategoryList();
			StringBuilder sb = new StringBuilder();

			for (int i = 0, l = cl.length; i < l; i++) {
				sb.append(i).append(",").append(cl[i][0][0]);
				for (int j = 0, m = cl[i].length; j < m; j++) {
					sb.append(";");
					sb.append(j).append(",").append(cl[i][j][0]);
				}
				if (i + 1 < l) {
					sb.append(";;");
				}
			}

			// ByteArrayOutputStream out = new ByteArrayOutputStream();
			// byte[] ziped = null;
			// try {
			// GZIPOutputStream gzout = new GZIPOutputStream(new
			// BufferedOutputStream(out));
			// gzout.write(sb.toString().getBytes(utf8));
			// gzout.finish();
			// out.close();
			// gzout.close();
			// ziped = out.toByteArray();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }

			// return new
			// String(Base64.byteArrayToBase64(sb.toString().getBytes()));

			return sb.toString().getBytes(utf8);

		} else if (input.startsWith("BoardList")) {
			String idstr = input.split(" ")[1];

			String[][][] cl = B2.getCategoryList();
			StringBuilder sb = new StringBuilder();
			int id = Integer.parseInt(idstr);
			for (int i = 0, l = cl[id].length; i < l; i++) {
				sb.append(i).append(",").append(cl[id][i][0]);
				if (i + 1 < l) {
					sb.append(";");
				}
			}
			return sb.toString().getBytes(utf8);

		} else if (input.startsWith("ThreadList")) {
			String categoryidstr = input.split(" ")[1];
			String boardidstr = input.split(" ")[2];

			String[][][] cl = B2.getCategoryList();
			StringBuilder sb = new StringBuilder();
			int categoryid = Integer.parseInt(categoryidstr);
			int boardid = Integer.parseInt(boardidstr);
			String[][] threadlist = B2.getThreadList(cl[categoryid][boardid]);
			for (int i = 0, l = threadlist.length; i < l; i++) {

				sb.append(i).append("<>").append(escapeHtml(threadlist[i][0]))
						.append("<>").append(threadlist[i][1]);
				if (i + 1 < l) {
					sb.append("><");
				}
			}
			return sb.toString().getBytes(utf8);
		} else if (input.startsWith("Thread")) {
			String threadurl = input.split(" ")[3];

			try {
				String reses = B2.getThreadDat(threadurl);
				return reses.getBytes(utf8);
			} catch (IOException e) {
				e.printStackTrace();
			}

			return new byte[2];
		} else {
			return new byte[2];
		}
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html
	 *            the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}
}

package jp.blogspot.simple_asta.b2.client;

import java.io.UnsupportedEncodingException;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class B2devel implements EntryPoint {

	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	public static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	public static String[][][] menu;
	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		SplitLayoutPanel sp = new SplitLayoutPanel();
		sp.setVisible(true);

		Tree tree = new Tree();
		ListBox list = new ListBox(true);
		 HTML detail = new HTML("");
		
		getCategoryTree(tree);
		tree.addSelectionHandler(new ThreadListSelectionHandler(
				greetingService, list));
		ScrollPanel scrollp = new ScrollPanel(tree);
		sp.addWest(scrollp, 140);

		list.setSize("100%", "100%");
		sp.addNorth(list, 200);
		list.addClickHandler(new ThreadClickHandler(this.greetingService, detail));

		ScrollPanel scrolp3 = new ScrollPanel(detail);
		sp.add(scrolp3);
		RootLayoutPanel.get().add(sp);

	}

	private void getCategoryTree(final Tree tree) {
		// Create the pop up dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Category");
		dialogBox.setAnimationEnabled(false);
		final Button closeButton = new Button("Close");

		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final HTML serverResponseLabel = new HTML();
		final VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
		serverResponseLabel.setText("");
		greetingService.greetServer("CategoryList",
				new AsyncCallback<byte[]>() {
					public void onFailure(Throwable caught) {
						dialogBox.setText("Remote Procedure Call - Failure");
						serverResponseLabel
								.addStyleName("serverResponseLabelError");
						serverResponseLabel.setHTML(B2devel.SERVER_ERROR);
						dialogBox.center();
						closeButton.setFocus(true);
					}

					@Override
					public void onSuccess(byte[] resultb) {
						String result = "";
						try {
							result = new String(resultb, "UTF-8");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}

						String[] categories = result.split(";;");

						for (String c : categories) {
							String[] c_borads = c.split(";");
							TreeItem item = tree.addTextItem(c_borads[0]
									.split(",")[1]);
							item.setUserObject(c_borads[0].split(",")[0]);
							for (int i = 2, l = c_borads.length; i < l; i++) {
								TreeItem boardNode = item
										.addTextItem(c_borads[i].split(",")[1]);
								boardNode.setUserObject(c_borads[i].split(",")[0]);

							}
						}
						tree.setVisible(true);
					}

					// @SuppressWarnings("resource")
					// private byte[] unZip(byte[] set) {
					// GZIPInputStream in;
					// try {
					// in = new GZIPInputStream((new ByteArrayInputStream(
					// set)));
					// final ByteArrayOutputStream outStream = new
					// ByteArrayOutputStream();
					// byte[] buffer = new byte[8192];
					// int iRead = 0;
					// while ((iRead = in.read(buffer, 0, 8192)) != -1) {
					// outStream.write(buffer, 0, iRead);
					// }
					// return outStream.toByteArray();
					// } catch (Exception e) {
					// e.printStackTrace();
					// }
					// return "".getBytes();
					// }
				});

	}
}

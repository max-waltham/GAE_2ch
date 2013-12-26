package jp.simple_asta.blog.b2.client;


import java.io.UnsupportedEncodingException;

import jp.simple_asta.blog.b2.client.java.ByteArrayInputStream;
import jp.simple_asta.blog.b2.client.java.ByteArrayOutputStream;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CategoryListHandler implements ClickHandler {
	private final GreetingServiceAsync greetingService;
	private final Button sendButton;

	public CategoryListHandler(final GreetingServiceAsync greetingService_,
			final Button sendButton_) {
		this.sendButton = sendButton_;
		this.greetingService = greetingService_;
	}

	/**
	 * Fired when the user clicks on the sendButton.
	 */
	public void onClick(ClickEvent event) {
		requestThreadList();
	}

	/**
	 * 
	 */
	private void requestThreadList() {

		sendButton.setEnabled(false);
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
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
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

					public void onSuccess(String result) {
						dialogBox.setText("CategoryList");
						serverResponseLabel
								.removeStyleName("serverResponseLabelError");

						String[] names = result.split(";");
						for (String name : names) {
							String[] t = name.split(",");
							final String tid = t[0];
							Button tButton = new Button(t[1]);
							tButton.addClickHandler(new BoardListHandler(
									greetingService, tid));
							dialogVPanel.add(tButton);

						}
						dialogBox.show();
						closeButton.setFocus(true);
					}

					@Override
					public void onSuccess(byte[] resultb) {
						dialogBox.setText("CategoryList");
						serverResponseLabel
								.removeStyleName("serverResponseLabelError");
						String result = "";
						try {
							result = new String(resultb, "UTF-8");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}

						String[] names = result.split(";");
						for (String name : names) {
							String[] t = name.split(",");
							final String tid = t[0];
							Button tButton = new Button(t[1]);
							tButton.addClickHandler(new BoardListHandler(
									greetingService, tid));
							dialogVPanel.add(tButton);

						}
						dialogBox.show();
						closeButton.setFocus(true);

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
					//	}
				});
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);
	}
}

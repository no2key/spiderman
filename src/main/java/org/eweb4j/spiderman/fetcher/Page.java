package org.eweb4j.spiderman.fetcher;

import java.io.UnsupportedEncodingException;

public class Page {

	/**
	 * The URL of this page.
	 */
	protected String url;

	/**
	 * The content of this page in binary format.
	 */
	protected byte[] contentData;
	
	protected String content;

	/**
	 * The ContentType of this page. For example: "text/html; charset=UTF-8"
	 */
	protected String contentType;

	/**
	 * The encoding of the content. For example: "gzip"
	 */
	protected String contentEncoding;

	/**
	 * The charset of the content. For example: "UTF-8"
	 */
	protected String contentCharset;

	public Page(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Returns the content of this page in binary format.
	 */
	public byte[] getContentData() {
		return contentData;
	}
	
	public String getContent(){
		if (this.contentData == null)
			return null;
		try {
			return new String(this.contentData, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		
		return new String(this.contentData);
	}

	public void setContentData(byte[] contentData) {
		this.contentData = contentData;
	}

	/**
	 * Returns the ContentType of this page. For example:
	 * "text/html; charset=UTF-8"
	 */
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * Returns the encoding of the content. For example: "gzip"
	 */
	public String getContentEncoding() {
		return contentEncoding;
	}

	public void setContentEncoding(String contentEncoding) {
		this.contentEncoding = contentEncoding;
	}

	/**
	 * Returns the charset of the content. For example: "UTF-8"
	 */
	public String getContentCharset() {
		return contentCharset;
	}

	public void setContentCharset(String contentCharset) {
		this.contentCharset = contentCharset;
	}

}

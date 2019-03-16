package com.honvay.hdms.dms.encryptor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author LIQIU
 * created on 2019/3/4
 **/
public interface Encryptors {

	InputStream encrypt(InputStream input);

	/**
	 * @param input
	 * @param output
	 * @throws IOException
	 */
	void encrypt(InputStream input, OutputStream output) throws IOException;

	InputStream decrypt(InputStream input);

	void decrypt(InputStream input, OutputStream output) throws IOException;


}

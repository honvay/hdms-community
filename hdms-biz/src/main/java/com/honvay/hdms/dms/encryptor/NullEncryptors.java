package com.honvay.hdms.dms.encryptor;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author LIQIU
 * created on 2019/3/4
 **/
public class NullEncryptors implements Encryptors {

	@Override
	public InputStream encrypt(InputStream input) {
		return input;
	}

	@Override
	public void encrypt(InputStream input, OutputStream output) throws IOException {
		IOUtils.copy(input, output);
	}

	@Override
	public InputStream decrypt(InputStream input) {
		return input;
	}

	@Override
	public void decrypt(InputStream input, OutputStream output) throws IOException {
		IOUtils.copy(input, output);
	}
}

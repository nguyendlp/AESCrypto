package com.crypto;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;

public class BcAes {

	PaddedBufferedBlockCipher encryptCipher;
	PaddedBufferedBlockCipher decryptCipher;

	// Buffer used to transport the bytes from one stream to another
	byte[] buf = new byte[16]; // input buffer
	byte[] obuf = new byte[512]; // output buffer

	byte[] key = null;

	public BcAes(String _key) throws UnsupportedEncodingException {
		// predefined 256 byte key value
		key = _key.getBytes("UTF-8");
		InitCiphers();
	}

	public BcAes(byte[] keyBytes) {
		key = new byte[keyBytes.length];
		System.arraycopy(keyBytes, 0, key, 0, keyBytes.length);
		InitCiphers();
	}

	private void InitCiphers() {
		encryptCipher = new PaddedBufferedBlockCipher(new AESEngine());
		encryptCipher.init(true, new KeyParameter(key));
		decryptCipher = new PaddedBufferedBlockCipher(new AESEngine());
		decryptCipher.init(false, new KeyParameter(key));
	}

	public void ResetCiphers() {
		if (encryptCipher != null)
			encryptCipher.reset();
		if (decryptCipher != null)
			decryptCipher.reset();
	}

	public void encrypt(InputStream in, OutputStream out) throws ShortBufferException, IllegalBlockSizeException,
			BadPaddingException, DataLengthException, IllegalStateException, InvalidCipherTextException {
		try {
			// Bytes written to out will be encrypted
			// Read in the cleartext bytes from in InputStream and
			// write them encrypted to out OutputStream

			int noBytesRead = 0; // number of bytes read from input
			int noBytesProcessed = 0; // number of bytes processed

			while ((noBytesRead = in.read(buf)) >= 0) {
				noBytesProcessed = encryptCipher.processBytes(buf, 0, noBytesRead, obuf, 0);
				out.write(obuf, 0, noBytesProcessed);
			}

			noBytesProcessed = encryptCipher.doFinal(obuf, 0);
			out.write(obuf, 0, noBytesProcessed);

			out.flush();
		} catch (java.io.IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void decrypt(InputStream in, OutputStream out) throws ShortBufferException, IllegalBlockSizeException,
			BadPaddingException, DataLengthException, IllegalStateException, InvalidCipherTextException {
		try {
			// Bytes read from in will be decrypted
			// Read in the decrypted bytes from in InputStream and and
			// write them in cleartext to out OutputStream

			int noBytesRead = 0; // number of bytes read from input
			int noBytesProcessed = 0; // number of bytes processed

			while ((noBytesRead = in.read(buf)) > 0) 
			{
				noBytesProcessed = decryptCipher.processBytes(buf, 0, noBytesRead, obuf, 0);
				out.write(obuf, 0, noBytesProcessed);
			}
			
			noBytesProcessed = decryptCipher.doFinal(obuf, 0);
			out.write(obuf, 0, noBytesProcessed);

			out.flush();
		} catch (java.io.IOException e) {
			System.out.println(e.getMessage());
		}
	}
}

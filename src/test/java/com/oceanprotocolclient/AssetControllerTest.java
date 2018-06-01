package com.oceanprotocolclient;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import com.oceanprotocolclient.asset.AssetController;

public class AssetControllerTest {
	@InjectMocks
	AssetController ac;

	 private GetMethod mockGetMethod;
     private HttpClient mockHttpClient;
     byte[] array = new byte[10];
     
     @Test
	public void TestMethod() {

		ac.downloadAsset(Mockito.any(URL.class), Mockito.anyString());

	}


     public void MockRequestHandler() throws HttpException, IOException {
         
         mockGetMethod = Mockito.mock(GetMethod.class);
         mockHttpClient = Mockito.mock(HttpClient.class);

         Mockito.when(
                 mockHttpClient.executeMethod(Mockito.any(GetMethod.class)))
                 .thenReturn(1);

         Mockito.when(mockGetMethod.getResponseBody()).thenReturn(array);
         ac.downloadAsset(Mockito.any(URL.class), Mockito.anyString());
     }

}

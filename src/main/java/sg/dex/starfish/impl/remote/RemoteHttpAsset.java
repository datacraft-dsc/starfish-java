package sg.dex.starfish.impl.remote;

import sg.dex.starfish.Asset;
import sg.dex.starfish.exception.GenericException;
import sg.dex.starfish.impl.ADataAsset;
import sg.dex.starfish.impl.memory.MemoryAsset;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * This clss is for reading the data from a give URL
 */
public class RemoteHttpAsset extends ADataAsset {

    private static Asset asset;

    protected RemoteHttpAsset(String meta) {
        super(meta);
    }

    /**
     * API to get the content of data with give url passes in param
     * @param urlString url where the content has to be read
     * @return
     */
    public static Asset createWithURL(String urlString) {
        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            throw new GenericException("MalformedURLException with cause: "+e.getMessage());
        }
        URLConnection conn ;
        StringBuilder result = new StringBuilder();
        try {
            conn = url.openConnection();
            InputStream is = conn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
            asset = MemoryAsset.create(result.toString());
            return asset;
        } catch (IOException e) {
            throw new GenericException("IO Exception with cause  : "+e.getMessage());
        }
    }


    @Override
    public long getContentSize() {
        return asset != null ? asset.getContent().length : 0;
    }

    @Override
    public InputStream getInputStream() {
        if (asset==null) throw new Error("RemoteHttpAsset has not been initialised with data");
        return new ByteArrayInputStream(asset.getContent());
    }
}

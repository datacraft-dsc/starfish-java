package sg.dex.starfish.impl.remote;

import sg.dex.starfish.Asset;
import sg.dex.starfish.exception.GenericException;
import sg.dex.starfish.impl.ADataAsset;
import sg.dex.starfish.impl.file.FileAsset;
import sg.dex.starfish.impl.memory.MemoryAsset;

import java.io.*;
import java.net.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;

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

    /**
     * API to create the Local Resource Asset with a given remote Asset path .
     * @param urlString
     * @return
     */
    public static Asset createResourceWithURL(String urlString ){

        String filename =null;
        try {
            filename = Paths.get(new URI(urlString).getPath()).getFileName().toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try {
            File f =downloadUsingNIO(urlString, filename);
            asset = FileAsset.create(f);
            return asset;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * API to donwload a resource file for a given ulr
     * @param urlStr
     * @param fileName
     * @return
     * @throws IOException
     */

    private static File downloadUsingNIO(String urlStr, String fileName) throws IOException {
        String path = RemoteHttpAsset.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String decodedPath = URLDecoder.decode(path, "UTF-8");
        URL url = new URL(urlStr);
        FileOutputStream fos =null;
        ReadableByteChannel rbc =null;
        File file = new File(decodedPath+File.separator+fileName);
        try{
         rbc = Channels.newChannel(url.openStream());

         fos = new FileOutputStream(file);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }
        finally {
            fos.close();
            rbc.close();
        }

        return file;
    }

    @Override
    public long getContentSize() {
        return asset != null ? asset.getContent().length : 0;
    }

    @Override
    public InputStream getContentStream() {
        if (asset==null) throw new Error("RemoteHttpAsset has not been initialised with data");
        return new ByteArrayInputStream(asset.getContent());
    }

}

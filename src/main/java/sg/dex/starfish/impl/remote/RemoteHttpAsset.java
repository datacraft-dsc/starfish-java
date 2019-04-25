//package sg.dex.starfish.impl.remote;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.StatusLine;
//import org.apache.http.client.methods.HttpGet;
//import sg.dex.starfish.Asset;
//import sg.dex.starfish.exception.*;
//import sg.dex.starfish.impl.file.FileAsset;
//import sg.dex.starfish.impl.memory.MemoryAsset;
//import sg.dex.starfish.util.DID;
//import sg.dex.starfish.util.HTTP;
//import sg.dex.starfish.util.Utils;
//
//import java.io.*;
//import java.net.*;
//import java.nio.channels.Channels;
//import java.nio.channels.ReadableByteChannel;
//import java.nio.file.Paths;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * This class is for reading the data from a given URL
// */
//public class RemoteHttpAsset extends ARemoteAsset {
//
//    private final RemoteAgent agent;
//
//    protected RemoteHttpAsset(String meta, RemoteAgent agent) {
//        super(meta);
//        this.agent=agent;
//    }
//
//    /**
//     * Creates a RemoteAsset with the given metadata on the specified remote agent
//     * @param agent RemoteAgent on which to create the RemoteAsset
//     * @param meta Asset metadata
//     * @return RemoteAsset
//     */
//    public static RemoteHttpAsset create(RemoteAgent agent, String meta) {
//        return new RemoteHttpAsset(meta,agent);
//    }
//
//    @Override
//    public boolean isDataAsset() {
//        return true;
//    }
//
//    /**
//     * Gets raw data corresponding to this Asset
//     *
//     * @throws AuthorizationException if requestor does not have access permission
//     * @throws StorageException if unable to load the Asset
//     * @return An input stream allowing consumption of the asset data
//     */
//    @Override
//    public InputStream getContentStream() {
//        URI uri = agent.getStorageURI(getAssetID());
//        HttpGet httpget = new HttpGet(uri);
//        agent.addAuthHeaders(httpget);
//        HttpResponse response = HTTP.execute(httpget);
//        StatusLine statusLine = response.getStatusLine();
//        int statusCode = statusLine.getStatusCode();
//        if (statusCode == 404) {
//            throw new RemoteException("Asset ID not found at: " + uri);
//        }
//        if (statusCode == 200) {
//            return HTTP.getContent(response);
//        }
//        throw new TODOException("status code not handled: " + statusCode);
//    }
//
//    /**
//     * Gets RemoteAsset size
//     *
//     * @throws TODOException not implemented yet
//     * @return size of the RemoteAsset
//     */
//    @Override
//    public long getContentSize() {
//        throw new TODOException();
//    }
//
//    @Override
//    public Map<String,Object> getParamValue() {
//        Map<String,Object> o=new HashMap<>();
//        // pass the asset ID, i.e. hash of content
//        o.put("did", getAssetDID());
//        return o;
//    }
//
//    /**
//     * Gets DID for this Asset
//     *
//     * @throws UnsupportedOperationException if unable to obtain DID
//     * @return DID
//     */
//    @Override
//    public DID getAssetDID() {
//        // DID of a remote asset is the DID of the appropriate agent with the asset ID as a resource path
//        DID agentDID=agent.getDID();
//        return agentDID.withPath(getAssetID());
//    }
//    public static Asset createWithURL(String urlString) {
//
//        // check url
//        if(!Utils.checkURL(urlString)){
//            throw new RemoteException("Give url is invalid :"+urlString;
//        }
//        //get data
//        // create Asset
//        URL url;
//        try {
//            url = new URL(urlString);
//        } catch (MalformedURLException e) {
//            throw new GenericException("MalformedURLException with cause: "+e.getMessage());
//        }
//        URLConnection conn ;
//        StringBuilder result = new StringBuilder();
//        try {
//            conn = url.openConnection();
//            InputStream is = conn.getInputStream();
//            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
//            String line;
//            while ((line = rd.readLine()) != null) {
//                result.append(line);
//            }
//            rd.close();
//            asset = MemoryAsset.create(result.toString());
//            return asset;
//        } catch (IOException e) {
//            throw new GenericException("IO Exception with cause  : "+e.getMessage());
//        }
//    }
//
//    /**
//     * API to create the Local Resource Asset with a given remote Asset path .
//     * @param urlString
//     * @return
//     */
//    public static Asset createResourceWithURL(String urlString ){
//
//        String filename =null;
//        try {
//            filename = Paths.get(new URI(urlString).getPath()).getFileName().toString();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//        try {
//            File f =downloadUsingNIO(urlString, filename);
//            asset = FileAsset.create(f);
//            return asset;
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * API to donwload a resource file for a given ulr
//     * @param urlStr
//     * @param fileName
//     * @return
//     * @throws IOException
//     */
//
//    private static File downloadUsingNIO(String urlStr, String fileName) throws IOException {
//        String path = RemoteHttpAsset.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//        String decodedPath = URLDecoder.decode(path, "UTF-8");
//        URL url = new URL(urlStr);
//        FileOutputStream fos =null;
//        ReadableByteChannel rbc =null;
//        File file = new File(decodedPath+File.separator+fileName);
//        try{
//         rbc = Channels.newChannel(url.openStream());
//
//         fos = new FileOutputStream(file);
//        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
//        }
//        finally {
//            fos.close();
//            rbc.close();
//        }
//
//        return file;
//    }
//
//    @
//    public static  InputStream getContentStream(URI uri) {
//        HttpGet httpget = new HttpGet(uri);
//        //httpget.addHeader().addAuthHeaders(httpget);
//        HttpResponse response = HTTP.execute(httpget);
//        StatusLine statusLine = response.getStatusLine();
//        int statusCode = statusLine.getStatusCode();
//        if (statusCode == 404) {
//            throw new RemoteException("Asset ID not found at: " + uri);
//        }
//        if (statusCode == 200) {
//            return HTTP.getContent(response);
//        }
//    }
//
//}

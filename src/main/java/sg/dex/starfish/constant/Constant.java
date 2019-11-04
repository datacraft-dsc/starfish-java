package sg.dex.starfish.constant;


@SuppressWarnings("javadoc")
public class Constant {

    //-----GENERAL---------------------
    public static final String CONTENT_HASH = "contentHash";
    public static final String TYPE = "type";
    public static final String DATE_CREATED = "dateCreated";
    public static final String SIZE = "size";
    public static final String CONTENT_TYPE = "contentType";
    public static final String ID = "id";
    public static final String CONTENTS = "contents";
    public static final String ASSET_ID = "assetID";

    public static final String DATA_SET = "dataset";
    public static final String BUNDLE = "bundle";


    public static final String NAME = "name";
    public static final String DID = "did";

    public static final String DID_METHOD = "op";
    public static final String SYNC = "sync";
    public static final String ASYNC = "async";
    public static final String MODES = "modes";
    public static final String OCTET_STREAM = "application/octet-stream";
    public static final String FILE = "file";
    public static final String PROVENANCE = "provenance";

    //-----JOB  -------

    // FIXME: confirm what are the actual job statuses in DEP6?
    public static final String SCHEDULED = "scheduled";
    public static final String SUCCEEDED = "succeeded";
    public static final String RUNNING = "running";
    public static final String FAILED = "failed";
	public static final String CANCELLED = "cancelled";
    
    public static final String OPERATION = "operation";
    public static final String PARAMS = "params";
    public static final String JOBS = "/jobs";
    public static final String JOB_ID = "jobid";
    public static final String STATUS = "status";


    //-------AUTH -----------
    public static final String USER = "user";
    public static final String USER_ID = "userid";
    public static final String TOKEN = "token";
    public static final String USER_NAME = "username";
    public static final String PASSWORD = "password";

    //--------MARKET ---------

    public static final String LISTING_URL = "/listings";
    public static final String PURCHASE_URL = "/purchases";
    public static final String DATA = "/data";
    public static final String LISTING_ID = "listingid";
    public static final String AGREEMENT = "agreement";

    //---------Listing status------------
    public static final String PUBLISHED = "published";
    public static final String UNPUBLISHED = "unpublished";
    public static final String SUSPENDED = "suspended";

    //-------Purchase status -------------
    public static final String WISHLIST = "wishlist";
    public static final String ORDERED = "ordered";
    public static final String DELIVERED = "delivered";

    //--------INVOKE -----------

    public static final String INVOKE_SYNC = "/sync";
    public static final String INVOKE_ASYNC = "/async";
	public static final Object RESULTS = "results";

    //----------SQUID CONSTANT----------------------

    public static final String DEFAULT_NAME = "Test_user";
    public static final String DEFAULT_LICENSE = "Test_license";
    public static final String DEFAULT_AUTHOR = "Test_author";
    public static final String DEFAULT_DATE_CREATED = "2012-10-10T17:00:000Z";
    public static final Object DEFAULT_PRICE = 5;
    
    //----------DDO Endpoints---------------------------------
	public static final String SERVICE = "service";
	public static final String SERVICE_ENDPOINT = "serviceEndpoint";
	public static final String ENDPOINT_STORAGE = "Ocean.Storage.v1";
	public static final String ENDPOINT_META = "Ocean.Meta.v1";
	public static final String ENDPOINT_MARKET = "Ocean.Market.v1";
	public static final String ENDPOINT_INVOKE = "Ocean.Invoke.v1";



}

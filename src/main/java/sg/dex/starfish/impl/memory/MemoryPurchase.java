package sg.dex.starfish.impl.memory;

import sg.dex.starfish.util.Utils;

import java.time.Instant;

public class MemoryPurchase {

    String info;
    String aggreemnt;
    private String id;
    private String trust_level;
    private String listingID;
    private String status;
    private Instant ctime;
    private Instant utime;

    private MemoryPurchase(String assetId) {
        this.id = Utils.createRandomHexString(16);
        this.listingID = assetId;
        ctime = Instant.now();
        utime = Instant.now();
        status = "unpublished";
        trust_level = "0";
    }

    public static MemoryPurchase create(String assetId) {

        return new MemoryPurchase(assetId);
    }

    public String getListingId() {
        return listingID;
    }

    public void setListingId(String assetId) {
        this.listingID = assetId;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "MemoryListing{" +
                "id='" + id + '\'' +
                ", assetId='" + listingID + '\'' +
                ", status='" + status + '\'' +
                ", ctime=" + ctime +
                ", utime=" + utime +
                '}';
    }

}

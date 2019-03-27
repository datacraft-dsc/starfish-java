package sg.dex.starfish.impl.memory;

import sg.dex.starfish.Account;
import sg.dex.starfish.Asset;
import sg.dex.starfish.Listing;
import sg.dex.starfish.impl.AListing;
import sg.dex.starfish.util.Utils;

import java.time.Instant;
import java.util.Map;

public class MemoryListing extends AListing {
    private String id;
    private String trust_level;
    private String assetId;
    private String status;
    private Instant ctime;
    private Instant utime;

    private MemoryListing(String assetId) {
        this.id = Utils.createRandomHexString(16);
        this.assetId = assetId;
        ctime= Instant.now();
        utime =Instant.now();
        status ="unpublished";
        trust_level="0";
    }

    public static MemoryListing create(String assetId) {

        return  new MemoryListing(assetId);
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    @Override
    public Asset getAsset() {
        return null;
    }

    @Override
    public Object getAgreement() {
        return null;
    }

    @Override
    public Map<String, Object> getInfo() {
        return null;
    }

    @Override
    public Asset purchase(Account account) {
        return null;
    }

    @Override
    public Listing refresh() {
        return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrust_level() {
        return trust_level;
    }

    public void setTrust_level(String trust_level) {
        this.trust_level = trust_level;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MemoryListing{" +
                "id='" + id + '\'' +
                ", trust_level='" + trust_level + '\'' +
                ", assetId='" + assetId + '\'' +
                ", status='" + status + '\'' +
                ", ctime=" + ctime +
                ", utime=" + utime +
                '}';
    }
}

package sg.dex.starfish.impl.squid.dao;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.oceanprotocol.squid.helpers.CryptoHelper;
import com.oceanprotocol.squid.models.DID;
import com.oceanprotocol.squid.models.Metadata;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder(alphabetic = true)
public class SquidAssetMetadata extends Metadata {

    @JsonProperty
    public DID did;

    public DID getDid() {
        return did;
    }

    public void setDid(DID did) {
        this.did = did;
    }

    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public Curation getCuration() {
        return curation;
    }

    public void setCuration(Curation curation) {
        this.curation = curation;
    }

    public Map<String, Object> getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(Map<String, Object> additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    @JsonProperty
    public Base base;
    @JsonProperty
    public Curation curation;
    @JsonProperty
    public Map<String, Object> additionalInformation = new HashMap<>();

    public SquidAssetMetadata() {
    }

    public SquidAssetMetadata(DID did) {
        this.did = did;
    }

    public static SquidAssetMetadata builder() {
        SquidAssetMetadata assetMetadata = new SquidAssetMetadata();
        assetMetadata.base = new Base();
        assetMetadata.curation = new Curation();
        return assetMetadata;
    }

    public String generateMetadataChecksum(String did) {

        String concatFields = this.base.files.stream()
                .map(file -> file.checksum != null ? file.checksum : "")
                .collect(Collectors.joining(""))
                .concat(this.base.name)
                .concat(this.base.author)
                .concat(this.base.license)
                .concat(did);
        return "0x" + CryptoHelper.sha3256(concatFields);


    }

    public enum assetTypes {dataset, algorithm, container, workflow, other}

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonPropertyOrder(alphabetic = true)
    public static class Base {

        @JsonProperty
        public String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Date getDateCreated() {
            return dateCreated;
        }

        public void setDateCreated(Date dateCreated) {
            this.dateCreated = dateCreated;
        }

        public Date getDatePublished() {
            return datePublished;
        }

        public void setDatePublished(Date datePublished) {
            this.datePublished = datePublished;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getLicense() {
            return license;
        }

        public void setLicense(String license) {
            this.license = license;
        }

        public String getCopyrightHolder() {
            return copyrightHolder;
        }

        public void setCopyrightHolder(String copyrightHolder) {
            this.copyrightHolder = copyrightHolder;
        }

        public String getWorkExample() {
            return workExample;
        }

        public void setWorkExample(String workExample) {
            this.workExample = workExample;
        }

        public ArrayList<File> getFiles() {
            return files;
        }

        public void setFiles(ArrayList<File> files) {
            this.files = files;
        }

        public String getEncryptedFiles() {
            return encryptedFiles;
        }

        public void setEncryptedFiles(String encryptedFiles) {
            this.encryptedFiles = encryptedFiles;
        }

        public ArrayList<Link> getLinks() {
            return links;
        }

        public void setLinks(ArrayList<Link> links) {
            this.links = links;
        }

        public String getInLanguage() {
            return inLanguage;
        }

        public void setInLanguage(String inLanguage) {
            this.inLanguage = inLanguage;
        }

        public ArrayList<String> getTags() {
            return tags;
        }

        public void setTags(ArrayList<String> tags) {
            this.tags = tags;
        }

        public ArrayList<String> getCategories() {
            return categories;
        }

        public void setCategories(ArrayList<String> categories) {
            this.categories = categories;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getChecksum() {
            return checksum;
        }

        public void setChecksum(String checksum) {
            this.checksum = checksum;
        }

        @JsonProperty
        public String type;

        @JsonProperty
        public String description;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
        @JsonProperty
        public Date dateCreated;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
        @JsonProperty
        public Date datePublished;

        @JsonProperty
        public String author;

        @JsonProperty
        public String license;

        @JsonProperty
        public String copyrightHolder;

        @JsonProperty
        public String workExample;

        @JsonProperty
        public ArrayList<File> files = new ArrayList<>();

        @JsonProperty
        public String encryptedFiles = null;

        @JsonProperty
        public ArrayList<Link> links = new ArrayList<>();

        @JsonProperty
        public String inLanguage;

        @JsonProperty
        public ArrayList<String> tags;

        @JsonProperty
        public ArrayList<String> categories;

        @JsonProperty
        public String price;

        @JsonProperty
        public String checksum;

        public Base() {
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonPropertyOrder(alphabetic = true)
    public static class Link {

        @JsonProperty
        public String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @JsonProperty
        public String type;

        @JsonProperty
        public String url;

        public Link() {
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonPropertyOrder(alphabetic = true)
    public static class Curation {

        @JsonProperty
        public float rating;

        public float getRating() {
            return rating;
        }

        public void setRating(float rating) {
            this.rating = rating;
        }

        public int getNumVotes() {
            return numVotes;
        }

        public void setNumVotes(int numVotes) {
            this.numVotes = numVotes;
        }

        public String getSchema() {
            return schema;
        }

        public void setSchema(String schema) {
            this.schema = schema;
        }

        public boolean isListed() {
            return isListed;
        }

        public void setListed(boolean listed) {
            isListed = listed;
        }

        @JsonProperty
        public int numVotes;

        @JsonProperty
        public String schema;

        @JsonProperty
        public boolean isListed;

        public Curation() {
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonPropertyOrder(alphabetic = true)
    public static class File {

        @JsonProperty
        public String contentType;

        @JsonProperty
        public Integer index;

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        public String getCompression() {
            return compression;
        }

        public void setCompression(String compression) {
            this.compression = compression;
        }

        public String getChecksum() {
            return checksum;
        }

        public void setChecksum(String checksum) {
            this.checksum = checksum;
        }

        public Integer getContentLength() {
            return contentLength;
        }

        public void setContentLength(Integer contentLength) {
            this.contentLength = contentLength;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getEncoding() {
            return encoding;
        }

        public void setEncoding(String encoding) {
            this.encoding = encoding;
        }

        @JsonProperty
        public String encoding;

        @JsonProperty
        public String compression;

        @JsonProperty
        public String checksum;

        @JsonProperty
        public Integer contentLength;

        @JsonProperty//(access = JsonProperty.Access.READ_ONLY)
        public String url;

        public File() {
        }
    }


}
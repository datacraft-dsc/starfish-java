package sg.dex.starfish.util;

import sg.dex.starfish.Asset;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProvUtil {

    public static String OPF = "opf";

    /**
     * Create a default namespace which includes the prefix and path for Ocean protocol schema
     *
     * @return a Map with prefixes respective IRIs used in this document
     */
    public static Map<String, Object> defaultPrefix() {
        Map<String, Object> prefix = new HashMap<String, Object>();
        prefix.put("xsd", "http://www.w3.org/2001/XMLSchema#");
        prefix.put("prov", "http://www.w3.org/ns/prov#");
        prefix.put(OPF, "http://oceanprotocol.com/schemas");

        Map<String, Object> pre = new HashMap<String, Object>();
        pre.put("prefix", prefix);
        return pre;
    }

    private static Map<String, Object> createAssetEntity(String id) {
        Map<String, Object> type = new HashMap<String, Object>();
        type.put("$", OPF + ":asset");
        type.put("type", "xsd:string");

        Map<String, Object> typeContainer = new HashMap<String, Object>();
        typeContainer.put("prov:type", type);
        Map<String, Object> thisContainer = new HashMap<String, Object>();
        thisContainer.put(OPF + ":" + id, typeContainer);
        return thisContainer;
    }

    /**
     * Returns a Entity referring to "this", the asset being registered
     * @return  Map
     */
    public static Map<String, Object> createThisInput() {
        return createAssetEntity("this");
    }

    /**
     * Returns a Entity referring to asset in the argument
     *
     * @param ast dependent asset
     * @return Map
     */
    public static Map<String, Object> createAssetDependency(Asset ast) {
        return createAssetEntity(ast.getAssetID());
    }

    private static Map<String, Object> jsonMapEntry(String key, Map<String, Object>[] ent) {
        Map<String, Object> eContainer = new HashMap<String, Object>();
        for (Map<String, Object> m : ent) {
            eContainer.putAll(m);
        }
        Map<String, Object> res = new HashMap<String, Object>();
        res.put(key, eContainer);
        return res;
    }

    /**
     * Returns a Map representing a list of entities
     * @param ent an array of entities to be added to the entity
     * @return Map
     */
    public static Map<String, Object> createEntities(Map<String, Object>... ent) {
        return jsonMapEntry("entity", ent);
    }

    /**
     * Returns a Map representing an entity, given the id and type
     *
     * @param agentId     the agent Id
     * @param agentType the type of account (e.g. ethereum account)
     * @return Map
     */
    public static Map<String, Object> createAgent(String agentId, AgentIdType agentType) {
        Map<String, Object> type = new HashMap<String, Object>();
        type.put("$", OPF + ":" + agentType.toString());
        type.put("type", "xsd:string");

        Map<String, Object> typeContainer = new HashMap<String, Object>();
        typeContainer.put("prov:type", type);
        Map<String, Object> thisContainer = new HashMap<String, Object>();
        thisContainer.put(OPF + ":" + agentId, typeContainer);
        return thisContainer;
    }

    /**
     * Returns a Map representing a list of agents
     * @param age age
     * @return Map
     *
     */
    public static Map<String, Object> createAgents(Map<String, Object>... age) {
        return jsonMapEntry("agent", age);
    }

    /**
     * Returns a Map representing an associatedWith Relationship, which
     * connects the agentID with the activityId
     *
     * @param agentId    the agent Id
     * @param activityId the activity Id
     * @return Map
     */
    public static Map<String, Object> associatedWith(String agentId, String activityId) {
        Map<String, Object> type = new HashMap<String, Object>();
        type.put("prov:agent", agentId);
        type.put("prov:activity", activityId);

        Map<String, Object> asid = new HashMap<String, Object>();
        asid.put("_:" + UUID.randomUUID().toString(), type);

        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("wasAssociatedWith", asid);
        return ret;
    }

    /**
     * Returns a Map representing an generatedBy Relationship, which
     * connects the entityid with the activityId
     *
     * @param activityId the activity Id
     * @return Map
     */
    public static Map<String, Object> generatedBy(String activityId) {
        String entityId=OPF+":this";
        Map<String, Object> type = new HashMap<String, Object>();
        type.put("prov:entity", entityId);
        type.put("prov:activity", activityId);

        Map<String, Object> asid = new HashMap<String, Object>();
        asid.put("_:" + UUID.randomUUID().toString(), type);

        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("wasGeneratedBy", asid);
        return ret;
    }

    /**
     * Returns a Map representing an derived by Relationship, which
     * connects the dependent entities
     *
     * @param entities list of entities that the generated entity is derived from
     * @return Map
     */
    public static Map<String, Object> derivedFrom(Collection<Asset> entities) {
        Map<String, Object> asid = new HashMap<String, Object>();
        for (Asset ast : entities) {
            Map<String, Object> type = new HashMap<String, Object>();
            type.put("prov:usedEntity", ast.getAssetID());
            type.put("prov:generatedEntity", OPF + "this");
            asid.put("_:" + UUID.randomUUID().toString(), type);
        }
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("wasDerivedFrom", asid);
        return ret;
    }

    /**
     * Returns an Activity identified by activity id and type
     *
     * @param actId        the activity Id
     * @param activityType The type of activity (e.g. publish or operation)
     * @return Map
     */
    public static Map<String, Object> createActivity(String actId, ActivityType activityType) {
        Map<String, Object> type = new HashMap<String, Object>();
        type.put("$", OPF + ":" + activityType.toString());
        type.put("type", "xsd:string");

        Map<String, Object> typeContainer = new HashMap<String, Object>();
        typeContainer.put("prov:type", type);
        Map<String, Object> thisContainer = new HashMap<String, Object>();
        thisContainer.put(OPF + ":" + actId, typeContainer);
        return thisContainer;
    }

    /**
     * Returns an Activity identified by activity id and type
     *
     * @param actId        the activity Id
     * @param activityType The type of activity (e.g. publish or operation)
     * @param actEntries   an array of activities
     * @return Map
     */
    public static Map<String, Object> createActivity(String actId, ActivityType activityType,
                                                     Map<String, Object> actEntries) {
        Map<String, Object> type = new HashMap<String, Object>();
        type.put("$", OPF + ":" + activityType.toString());
        type.put("type", "xsd:string");

        Map<String, Object> typeContainer = new HashMap<String, Object>();
        typeContainer.put("prov:type", type);
        typeContainer.putAll(actEntries);
        Map<String, Object> thisContainer = new HashMap<String, Object>();
        thisContainer.put(OPF + ":" + actId, typeContainer);
        return thisContainer;
    }

    /**
     * Returns a list of Activities
     * @param acts array of activities
     * @return Map
     */
    public static Map<String, Object> createActivities(Map<String, Object>... acts) {
        return jsonMapEntry("activity", acts);
    }

    /**
     * Create default provenance metadata for publishing an asset. This create a random
     * activity ID for tracking the provenance
     *
     * @param agentId the agent identifier
     * @return Map
     */
    public static Map<String, Object> createPublishProvenance(String agentId) {
        String actId = UUID.randomUUID().toString();
        return createPublishProvenance(actId, agentId);
    }

    /**
     * Create default provenance metadata for publishing an asset
     *
     * @param actId   the activity Id
     * @param agentId the agent identifier
     * @return the prov metadata encoded in a Map
     */
    public static Map<String, Object> createPublishProvenance(String actId,
                                                              String agentId) {
        Map<String, Object> a = defaultPrefix();
        Map<String, Object> e = createEntities(createThisInput());
        Map<String, Object> act = createActivity(actId, ActivityType.PUBLISH);
        Map<String, Object> agent = createAgent(agentId, AgentIdType.ACCOUNT);

        a.putAll(createActivities(act));
        a.putAll(e);
        a.putAll(createAgents(agent));
        a.putAll(associatedWith(agentId, actId));
        a.putAll(generatedBy(actId));
        return a;
    }

    /**
     * Create dependencies. There are 2 sets of dependencies: one that
     * are assets, and the second which is JSON payloads.
     *
     * @param params          the param string sent to the invoke rest API
     * @param resultParamName the param name of the generated asset
     * @return the input and output encoded as a Map
     */
    public static Map<String, Object> createDependencies(String params,
                                                         String resultParamName) {
        Map<String, Object> ret = new HashMap<String, Object>();
        Map<String, Object> p = new HashMap<String, Object>();
        p.put("$", resultParamName);
        p.put("type", "xsd:string");
        ret.put(OPF + ":resultParamName", p);

        p = new HashMap<String, Object>();
        p.put("$", params);
        p.put("type", "xsd:string");
        ret.put(OPF + ":params", p);

        return ret;
    }

    /**
     * Create default provenance metadata for publishing an asset created using invoke.
     *
     * @param actId             the activity Id
     * @param agentId           the agent identifier
     * @param assetDependencies the list of assets that consumed by the operation
     * @param params            the argument string passed to the operation
     * @param resultParamName   the name of the result argument for the generated asset
     * @return Map
     */
    public static Map<String, Object> createInvokeProvenance(String actId, String agentId,
                                                             Collection<Asset> assetDependencies,
                                                             String params,
                                                             String resultParamName) {
        Map<String, Object> a = defaultPrefix();
        Map<String, Object> e = new HashMap<String, Object>();
        e.putAll(createThisInput());
        for (Asset k : assetDependencies) {
            e.putAll(createAssetDependency(k));
        }
        Map<String, Object> deps = createDependencies(params, resultParamName);

        Map<String, Object> act = createActivity(actId, ActivityType.OPERATION, deps);
        Map<String, Object> agent = createAgent(agentId, AgentIdType.ACCOUNT);

        a.putAll(createEntities(e));
        a.putAll(createActivities(act));
        a.putAll(createAgents(agent));
        a.putAll(associatedWith(agentId, actId));
        if (assetDependencies.size() >  0){
            a.putAll(derivedFrom(assetDependencies));
        }
        a.putAll(generatedBy(actId));
        return a;
    }

    public enum AgentIdType {ACCOUNT, ETHEREUMACCOUNT}

    public enum ActivityType {PUBLISH, IMPORT, OPERATION}
}

package sg.dex.starfish.util;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ProvUtil{



    public static String OPF="opf";
    /*
     * Create a default namespace which includes the prefix and path for Oceanprotocol schema
     */
    public static Map<String,Object> defaultPrefix(){
        Map<String,Object> prefix=new HashMap<String,Object>();
        prefix.put("xsd","http://www.w3.org/2001/XMLSchema#");
        prefix.put("prov","http://www.w3.org/ns/prov#");
        prefix.put(OPF,"http://oceanprotocol.com/schemas");

        Map<String,Object> pre=new HashMap<String,Object>();
        pre.put("prefix",prefix);
        return pre;
    }


    /*
     * Returns a Entity referring to "this", the asset being registered
     */
    public static Map<String,Object> createThisInput(){
        Map<String,Object> type=new HashMap<String,Object>();
        type.put("$",OPF+":asset");
        type.put("type","xsd:string");

        Map<String,Object> typeContainer=new HashMap<String,Object>();
        typeContainer.put("prov:type",type);
        Map<String,Object> thisContainer=new HashMap<String,Object>();
        thisContainer.put(OPF+":this",typeContainer);
        return thisContainer;
    }


    private static Map<String,Object> jsonMapEntry(String key,Map<String,Object> ent[]){
        Map<String,Object> eContainer=new HashMap<String,Object>();
        for (Map<String,Object> m:ent){
            eContainer.putAll(m);
        }
        Map<String,Object> res=new HashMap<String,Object>();
        res.put(key,eContainer);
        return res;
    }

    /*
     * Returns a Map representing a list of entities 
     */
    public static Map<String,Object> createEntities(Map<String,Object>... ent){
        return jsonMapEntry("entity",ent);
    }

    public static enum AgentIdType { ACCOUNT, ETHEREUMACCOUNT};

    /*
     * Returns a Map representing an entity, given the id and type
     */
    public static Map<String,Object> createAgent(String agentId,AgentIdType agentType){
        Map<String,Object> type=new HashMap<String,Object>();
        type.put("$",OPF+":"+agentType.toString());
        type.put("type","xsd:string");

        Map<String,Object> typeContainer=new HashMap<String,Object>();
        typeContainer.put("prov:type",type);
        Map<String,Object> thisContainer=new HashMap<String,Object>();
        thisContainer.put(OPF+":"+agentId,typeContainer);
        return thisContainer;
    }

    /*
     * Returns a Map representing a list of agents
     */
    public static Map<String,Object> createAgents(Map<String,Object>... age){
        return jsonMapEntry("agent",age);
    }

    /*
     * Returns a Map representing an associatedWith Relationship, which
     * connects the agentID with the activityId
     */
    public static Map<String,Object> associatedWith(String agentId, String activityId){
        Map<String,Object> type=new HashMap<String,Object>();
        type.put("prov:agent",agentId);
        type.put("prov:activity",activityId);
        
        Map<String,Object> asid=new HashMap<String,Object>();
        asid.put("_:"+UUID.randomUUID().toString(),type);

        Map<String,Object> ret=new HashMap<String,Object>();
        ret.put("wasAssociatedWith",asid);
        return ret;
    }

    /*
     * Returns a Map representing an generatedBy Relationship, which
     * connects the entityid with the activityId
     */
    public static Map<String,Object> generatedBy(String entityId, String activityId){
        Map<String,Object> type=new HashMap<String,Object>();
        type.put("prov:entity",entityId);
        type.put("prov:activity",activityId);
        
        Map<String,Object> asid=new HashMap<String,Object>();
        asid.put("_:"+UUID.randomUUID().toString(),type);

        Map<String,Object> ret=new HashMap<String,Object>();
        ret.put("wasGeneratedBy",asid);
        return ret;
    }

    public static enum ActivityType { PUBLISH, IMPORT, OPERATION};

    /*
     * Returns an Activity identified by activity id and type
     */
    public static Map<String,Object> createActivity(String actId,ActivityType activityType){
        Map<String,Object> type=new HashMap<String,Object>();
        type.put("$",OPF+":"+activityType.toString());
        type.put("type","xsd:string");

        Map<String,Object> typeContainer=new HashMap<String,Object>();
        typeContainer.put("prov:type",type);
        Map<String,Object> thisContainer=new HashMap<String,Object>();
        thisContainer.put(OPF+":"+actId,typeContainer);
        return thisContainer;
    }

    /*
     * Returns a list of Activities 
     */
    public static Map<String,Object> createActivities(Map<String,Object>... acts){
        return jsonMapEntry("activity",acts);
    }

    /**
     * Create a default Prov object with the prefixes and the "this" entity
     */
    public static Map<String,Object> getBaseProvObject(){
        Map<String,Object> e = createEntities(createThisInput());
        Map<String,Object> res=defaultPrefix();
        res.putAll(e);
        return res;
    }

    /**
     * Create default provenance metadata for publishing an asset
     */
    public static Map<String,Object> createPublishProvenance(String actId,
                                        String agentId) {
        Map<String,Object> a=getBaseProvObject();
        Map<String,Object> act=createActivity(actId,ActivityType.PUBLISH);
        Map<String,Object> agent=createAgent(agentId,AgentIdType.ACCOUNT);

        a.putAll(createActivities(act));
        a.putAll(createAgents(agent));
        a.putAll(associatedWith(agentId,actId));
        a.putAll(generatedBy("this",actId));
        return a;
    }
}

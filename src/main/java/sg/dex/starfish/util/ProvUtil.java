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
    public static JSONObject defaultPrefix(){
        JSONObject prefix=new JSONObject();
        prefix.put("xsd","http://www.w3.org/2001/XMLSchema#");
        prefix.put("prov","http://www.w3.org/ns/prov#");
        prefix.put(OPF,"http://oceanprotocol.com/schemas");

        JSONObject pre=new JSONObject();
        pre.put("prefix",prefix);
        return pre;
    }


    /*
     * Returns a Entity referring to "this", the asset being registered
     */
    public static JSONObject createThisInput(){
        JSONObject type=new JSONObject();
        type.put("$",OPF+":asset");
        type.put("type","xsd:string");

        JSONObject typeContainer=new JSONObject();
        typeContainer.put("prov:type",type);
        JSONObject thisContainer=new JSONObject();
        thisContainer.put(OPF+":this",typeContainer);
        return thisContainer;
    }


    private static JSONObject jsonMapEntry(String key,JSONObject ent[]){
        JSONObject eContainer=new JSONObject();
        for (Map<String,Object> m:ent){
            eContainer.putAll(m);
        }
        JSONObject res=new JSONObject();
        res.put(key,eContainer);
        return res;
    }

    /*
     * Returns a JSON Object representing a list of entities 
     */
    public static JSONObject createEntities(JSONObject... ent){
        return jsonMapEntry("entity",ent);
    }

    public static enum AgentIdType { ACCOUNT, ETHEREUMACCOUNT};

    /*
     * Returns a JSON Object representing an entity, given the id and type
     */
    public static JSONObject createAgent(String agentId,AgentIdType agentType){
        JSONObject type=new JSONObject();
        type.put("$",OPF+":"+agentType.toString());
        type.put("type","xsd:string");

        JSONObject typeContainer=new JSONObject();
        typeContainer.put("prov:type",type);
        JSONObject thisContainer=new JSONObject();
        thisContainer.put(OPF+":"+agentId,typeContainer);
        return thisContainer;
    }

    /*
     * Returns a JSON Object representing a list of agents
     */
    public static JSONObject createAgents(JSONObject... age){
        return jsonMapEntry("agent",age);
    }

    /*
     * Returns a JSON Object representing an associatedWith Relationship, which
     * connects the agentID with the activityId
     */
    public static JSONObject associatedWith(String agentId, String activityId){
        JSONObject type=new JSONObject();
        type.put("prov:agent",agentId);
        type.put("prov:activity",activityId);
        
        JSONObject asid=new JSONObject();
        asid.put("_:"+UUID.randomUUID().toString(),type);

        JSONObject ret=new JSONObject();
        ret.put("wasAssociatedWith",asid);
        return ret;
    }

    /*
     * Returns a JSON Object representing an generatedBy Relationship, which
     * connects the entityid with the activityId
     */
    public static JSONObject generatedBy(String entityId, String activityId){
        JSONObject type=new JSONObject();
        type.put("prov:entity",entityId);
        type.put("prov:activity",activityId);
        
        JSONObject asid=new JSONObject();
        asid.put("_:"+UUID.randomUUID().toString(),type);

        JSONObject ret=new JSONObject();
        ret.put("wasGeneratedBy",asid);
        return ret;
    }

    public static enum ActivityType { PUBLISH, IMPORT, OPERATION};

    /*
     * Returns an Activity identified by activity id and type
     */
    public static JSONObject createActivity(String actId,ActivityType activityType){
        JSONObject type=new JSONObject();
        type.put("$",OPF+":"+activityType.toString());
        type.put("type","xsd:string");

        JSONObject typeContainer=new JSONObject();
        typeContainer.put("prov:type",type);
        JSONObject thisContainer=new JSONObject();
        thisContainer.put(OPF+":"+actId,typeContainer);
        return thisContainer;
    }

    /*
     * Returns a list of Activities 
     */
    public static JSONObject createActivities(JSONObject... acts){
        return jsonMapEntry("activity",acts);
    }

    /**
     * Create a default Prov object with the prefixes and the "this" entity
     */
    public static JSONObject getBaseProvObject(){
        JSONObject e = createEntities(createThisInput());
        JSONObject res=defaultPrefix();
        res.putAll(e);
        return res;
    }

    /**
     * Create default provenance metadata for publishing an asset
     */
    public static JSONObject createPublishProvenance(String actId,
                                        String agentId) {
        JSONObject a=getBaseProvObject();
        JSONObject act=createActivity(actId,ActivityType.PUBLISH);
        JSONObject agent=createAgent(agentId,AgentIdType.ACCOUNT);

        a.putAll(createActivities(act));
        a.putAll(createAgents(agent));
        a.putAll(associatedWith(agentId,actId));
        a.putAll(generatedBy("this",actId));
        return a;
    }
}

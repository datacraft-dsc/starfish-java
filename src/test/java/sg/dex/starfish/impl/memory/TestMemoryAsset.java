package sg.dex.starfish.impl.memory;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import sg.dex.crypto.Hash;
import sg.dex.starfish.Asset;
import sg.dex.starfish.util.DID;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestMemoryAsset {

	MemoryAsset memoryAsset;
	@Before
	public void setup(){

		memoryAsset = MemoryAsset.create(new byte[] {1,2,3});

	}
	@Test public void testCreation() {
		int SIZE=10;
		
		byte[] data=new byte[SIZE];
		
		MemoryAsset a=MemoryAsset.create(data);
		assertEquals(SIZE,a.getContentSize());
		Map<String,Object> meta=a.getMetadata();
		assertEquals(Integer.toString(SIZE),meta.get("size"));
		assertEquals(Hash.keccak256String(data),meta.get("contentHash"));
	}
	
	@Test public void testDID() {
		byte[] data=new byte[] {1,2,3};
		Asset a=MemoryAsset.create(data);
		try {
			DID did=a.getAssetDID(); // in-memory assets don't have a DID, so this should fail
			fail("Should not succeed! Got: "+did);
		} catch (UnsupportedOperationException ex) {
			/* OK */
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullData() {
		byte[] data=null;
		MemoryAsset.create(data);

	}
	@Test
	public void testBuildMetaData(){

		byte[] data=new byte[] {1,2,3};
		Map<String,Object> metaMap = new HashMap<>();
		metaMap.put("test1","success");
		Asset a=MemoryAsset.create(data,metaMap);
		assertEquals(a.getMetadata().get("test1"), "success");
	}

	@Test
	public void testCreateAsset(){
		MemoryAsset.create(memoryAsset);
		assertNotNull(memoryAsset.getAssetID());
	}
	@Test
	public void testGetInputStream(){
		assertNotNull(memoryAsset.getContentStream());
	}
	@Test
	public void testgetContent(){
		assertNotNull(memoryAsset.getContent());
	}
	@Test
	public void testgetContentSize(){
		assertNotNull(memoryAsset.getContentSize());
	}
}

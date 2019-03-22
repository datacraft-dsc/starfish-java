package sg.dex.starfish.impl;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import sg.dex.crypto.Hash;
import sg.dex.starfish.Asset;
import sg.dex.starfish.impl.memory.MemoryAsset;
import sg.dex.starfish.util.DID;

import static org.junit.Assert.*;

public class TestMemoryAsset {

	Asset asset;
	MemoryAsset memoryAsset;
	@Before
	public void setup(){

		asset = new Asset() {
			@Override
			public String getAssetID() {
				return "test";
			}

			@Override
			public DID getAssetDID() {
				return null;
			}

			@Override
			public Map<String, Object> getMetadata() {
				return null;
			}

			@Override
			public boolean isDataAsset() {
				return false;
			}

			@Override
			public boolean isOperation() {
				return false;
			}

			@Override
			public String getMetadataString() {
				return null;
			}

			@Override
			public Map<String, Object> getParamValue() {
				return null;
			}
		};

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
		Asset a=MemoryAsset.create(metaMap,data);
		assertEquals(metaMap.get("test1"), "success");
	}
	@Test(expected = IllegalArgumentException.class)
	public void testCreateAssetException(){
		MemoryAsset.create(asset);
		//assertEquals(memoryAsset.getAssetID(),"test");
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

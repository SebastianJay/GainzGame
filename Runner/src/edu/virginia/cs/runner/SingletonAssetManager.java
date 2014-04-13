package edu.virginia.cs.runner;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class SingletonAssetManager {

	private Map<String, String> map;
	private AssetManager manager;
	private FileHandle fileHandle;
	
	private SingletonAssetManager() {
		super();

		map = new HashMap<String, String>();
		manager = new AssetManager();
		
		manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
	}
	
	private static SingletonAssetManager instance;
	public static SingletonAssetManager getInstance() {
		if(instance == null) {
			instance = new SingletonAssetManager();
		}
		
		return instance;
	}
	
	public void load(String name, String fileName, Class<?> type) {
		manager.load(fileName, type);
		map.put(name, fileName);
	}
	
	public <T> T get(String name) {
		String fileName = map.get(name);
		return manager.get(fileName);
	}
	
	public void finishLoading() {
		manager.finishLoading();
	}
	
	public FileHandle getModelData() {
		return fileHandle;
	}
	
	public void loadModelData(String filename) {
		fileHandle = Gdx.files.internal(filename);
	}
}

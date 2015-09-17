package com.alibaba.middleware.race.rpc.tool;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * LRU的MAP最近最常用的保留
 * @author sei.zz
 *
 * @param <K>
 * @param <V>
 */
public class LRUMap<K, V> extends LinkedHashMap<K, V> implements Map<K, V> {
	private static final long serialVersionUID = -188971896404993320L;
	private int maxSize;

	public LRUMap(int maxSize) {
		super((int) Math.ceil((1f * maxSize) / 0.75f) + 16, 0.75f, true);
		this.maxSize = maxSize;
	}

	@Override
	protected synchronized boolean removeEldestEntry(
			java.util.Map.Entry<K, V> eldest) {
		boolean delete = size() > maxSize;
		return delete;
	}

	@Override
	public synchronized int size() {
		return super.size();
	}

	@Override
	public synchronized V get(Object key) {
		return super.get(key);
	}

	@Override
	public synchronized V remove(Object key) {
		return super.remove(key);
	}

	@Override
	public synchronized void clear() {
		super.clear();
	}

	@Override
	public synchronized boolean isEmpty() {
		return super.isEmpty();
	}

	@Override
	public synchronized boolean containsKey(Object key) {
		return super.containsKey(key);
	}

	@Override
	public synchronized boolean containsValue(Object value) {
		return super.containsValue(value);
	}

	@Override
	public synchronized V put(K key, V value) {
		return super.put(key, value);
	}

	@Override
	public synchronized void putAll(Map<? extends K, ? extends V> m) {
		super.putAll(m);
	}

	@Override
	public synchronized Set<K> keySet() {
		return super.keySet();
	}

	@Override
	public synchronized Collection<V> values() {
		return super.values();
	}

	@Override
	public synchronized Set<java.util.Map.Entry<K, V>> entrySet() {
		return super.entrySet();
	}
}

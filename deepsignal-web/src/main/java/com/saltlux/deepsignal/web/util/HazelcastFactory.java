//package com.saltlux.deepsignal.web.util;
//
//import com.hazelcast.core.HazelcastInstance;
//import com.hazelcast.map.IMap;
//import java.util.concurrent.TimeUnit;
//
//public class HazelcastFactory<K, V> {
//
//    private IMap<K, V> mapCodeSms;
//    private static HazelcastFactory instance = null;
//
//    public HazelcastFactory(IMap<K, V> mapCodeSms) {
//        this.mapCodeSms = mapCodeSms;
//    }
//
//    public static HazelcastFactory getInstance(HazelcastInstance hazelcastInstance, String nameMap) {
//        if (instance == null) {
//            instance = new HazelcastFactory(hazelcastInstance.getMap(nameMap));
//        }
//        return instance;
//    }
//
//    public V putValue(K key, V value) {
//        return mapCodeSms.put(key, value);
//    }
//
//    public V putValueWithExpireTime(K key, V value, long timeout, TimeUnit unit) {
//        return mapCodeSms.put(key, value, timeout, unit);
//    }
//
//    public V getDataByKey(K key) {
//        return mapCodeSms.get(key);
//    }
//
//    public void update(K key, V value) {
//        mapCodeSms.set(key, value);
//    }
//
//    public V deleteData(K key) {
//        return mapCodeSms.remove(key);
//    }
//}

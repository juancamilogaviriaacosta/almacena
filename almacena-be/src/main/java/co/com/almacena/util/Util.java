package co.com.almacena.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.query.NativeQuery;

import jakarta.persistence.EntityManager;

public class Util {
	
	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> getResultList(EntityManager em, String sql, Map<String, Object> params) {
	    NativeQuery<?> query = em.createNativeQuery(sql).unwrap(NativeQuery.class);
	    if (params != null) {
	        params.forEach(query::setParameter);
	    }

	    query.setTupleTransformer((tuple, aliases) -> {
	        Map<String, Object> map = new LinkedHashMap<>();
	        for (int i = 0; i < aliases.length; i++) {
	            map.put(aliases[i], tuple[i]);
	        }
	        return map;
	    });
	    return (List<Map<String, Object>>) query.getResultList();
	}

}

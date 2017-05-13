package com.sprzny.meitu.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import android.util.Log;

import com.dodola.model.DuitangInfo;
import com.dodowaterfall.Helper;

public class SprznyService {
    
    /**
     * 精选
     * 
     * @return
     */
    public static List<DuitangInfo> mmonly(int page, int categoryid) {
        String url = "http://www.sprzny.com/mmonly/l/" ;
        if (categoryid != 0) {
            url = url + categoryid + "/" + page;
        } else {
            url = url + page;
        }
        
        String json = "";
        try {
            json = Helper.getStringFromUrl(url);
        } catch (IOException e) {
            Log.e("IOException is : ", e.toString());
            e.printStackTrace();
            return new ArrayList<DuitangInfo>();
        }
        
        return parseNewsJSON(json);
    }
    
    /**
     * 解析json
     * 
     * @param json
     * @return
     */
    private static List<DuitangInfo> parseNewsJSON(String json) {
        List<DuitangInfo> duitangs = new ArrayList<DuitangInfo>();
        try {
            if (null != json) {
                
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNodes = mapper.readValue(json, JsonNode.class);
                if (jsonNodes != null) {
                    for (JsonNode node : jsonNodes) {
                        duitangs.add(mapper.readValue(node, DuitangInfo.class));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return duitangs;
    }
}

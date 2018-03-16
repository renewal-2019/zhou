package org.xiaofan.zhou.vo.factory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.xiaofan.zhou.util.PropertyReaderUtil;
import org.xiaofan.zhou.vo.AShore;
import org.xiaofan.zhou.vo.Task;

import java.util.*;
import java.util.stream.IntStream;

/**
 * 岸桥生产工厂
 */
public class AShoreFactory {

    public static List<AShore> shores = new ArrayList<>();

    //单例
    private static AShoreFactory AShoreFactory = new AShoreFactory();
    //私有化构造方法
    private AShoreFactory(){}

    //定义一个对外开放的方法，以获取该工厂实例
    public static AShoreFactory getShoreFactory(){
        return AShoreFactory;
    }

    /**
     * 创建岸桥实体
     * @param id
     * @return
     */
    private AShore createShore(int id){
        return new AShore(id);
    }

    /**
     * 批量创建岸桥
     * @param count
     * @return
     */
    public List<AShore> batchCreateShore(int count){
        //岸桥到充电站的距离
        JSONArray distances2Station = PropertyReaderUtil.readYml().getJSONArray("distanceOfAShore2Station");
        //岸桥和厂桥的距离
        JSONObject distance2CBridge = PropertyReaderUtil.readYml().getJSONObject("distanceOfBridge");
        //岸桥到场桥的任务
        JSONObject task = PropertyReaderUtil.readYml().getJSONObject("task");

        //按数量创建岸桥
        for (int i = 1; i < count + 1; i++) {
            AShore shore = createShore(i);
            int startIndex = 0;
            //设置到充电站的距离
            shore.setDistanceOfStation(Integer.valueOf(distances2Station.get(i-1).toString()));
            Map<String,List<Task>> tasks = new HashMap<>();
            //分配任务到各个岸桥
            for (int j = 1; j < 7; j++) {
                String key = i + String.valueOf(j);
                Integer integer = Integer.valueOf(task.get(key).toString());
                List<Task> taskList = new ArrayList<>();
                //从总任务中分配
                for (int k = startIndex; k < integer.intValue(); k++) {
                    taskList.add(TaskFactory.tasks.get(k));
                }
                tasks.put(key, taskList);
                startIndex = integer;
            }
            shore.setTasks(tasks);
            Map<String,Integer> distanceOfCBridge = new HashMap<>();
            //设置到场桥的距离
            for (int j = 1; j < 7; j++) {
                String key = i+String.valueOf(j);
                distanceOfCBridge.put(key,Integer.valueOf(distance2CBridge.get(key).toString()));
            }
            shore.setDistanceOfCBridge(distanceOfCBridge);
            shores.add(shore);
        }
        return shores;
    }

    /**
     * @desc 为每个岸桥分配任务
     * @author maokeluo
     * @methodName assignedTasks
     * @create 18-3-15
     * @return void
     */
    public void assignedTasks(){
        //TODO
    }
}

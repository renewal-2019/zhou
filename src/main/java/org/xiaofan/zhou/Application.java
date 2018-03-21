package org.xiaofan.zhou;

import org.xiaofan.zhou.vo.*;
import org.xiaofan.zhou.vo.factory.AGVFactory;
import org.xiaofan.zhou.vo.factory.AShoreFactory;
import org.xiaofan.zhou.vo.factory.CBridgeFactory;
import org.xiaofan.zhou.vo.factory.TaskFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author maokeluo
 * @desc
 * @create 18-3-13
 */
public class Application {


    public static void main(String[] args) {

        //利用工厂创建任务
        TaskFactory taskFactory = TaskFactory.getTaskFactory();
        List<Task> tasks = taskFactory.batchCreateTask(1000);
        //tasks.forEach(System.out::println);

        //利用工厂创建场桥
        CBridgeFactory cBridgeFactory = CBridgeFactory.getCBridgeFactory();
        List<CBridge> cBridges = cBridgeFactory.batchCreateBridge(6);
        //cBridges.forEach(System.out::println);

        //利用工厂创建岸桥
        AShoreFactory aShoreFactory = AShoreFactory.getShoreFactory();
        List<AShore> aShores = aShoreFactory.batchCreateShore(2);
        //aShores.forEach(System.out::println);

        //初始化充电站
        ChargeStation.newInstance();

        //利用工厂创建AGV
        AGVFactory agvFactory = AGVFactory.getAgvFactory();
        List<AGV> agvs = agvFactory.batchCreateAGV(8);
        //agvs.forEach(System.out::println);

        AtomicBoolean stop = new AtomicBoolean(true);
        //每个AGV开启一个线程去执行任务
        ExecutorService executorService1 = Executors.newSingleThreadExecutor();
        Runnable task1 = ()-> {
            while (stop.get()){
                taskFactory.accessTask(agvs.get(0));
                agvs.get(0).run();
            }
        };
        executorService1.submit(task1);

        ExecutorService executorService2 = Executors.newSingleThreadExecutor();
        Runnable task2 = ()->{
            while (stop.get()){
                taskFactory.accessTask(agvs.get(1));
                agvs.get(1).run();
            }
        };
        executorService2.submit(task2);

        ExecutorService executorService3 = Executors.newSingleThreadExecutor();
        Runnable task3 = ()->{
            while (stop.get()){
                taskFactory.accessTask(agvs.get(2));
                agvs.get(2).run();
            }
        };
        executorService3.submit(task3);

        ExecutorService executorService4 = Executors.newSingleThreadExecutor();
        Runnable task4 = ()->{
            while (stop.get()){
                taskFactory.accessTask(agvs.get(3));
                agvs.get(3).run();
            }
        };
        executorService4.submit(task4);

        ExecutorService executorService5 = Executors.newSingleThreadExecutor();
        Runnable task5 = ()->{
            while (stop.get()){
                taskFactory.accessTask(agvs.get(4));
                agvs.get(4).run();
            }
        };
        executorService5.submit(task5);

        ExecutorService executorService6 = Executors.newSingleThreadExecutor();
        Runnable task6 = ()->{
            while (stop.get()){
                taskFactory.accessTask(agvs.get(5));
                agvs.get(5).run();
            }
        };
        executorService6.submit(task6);

        ExecutorService executorService7 = Executors.newSingleThreadExecutor();
        Runnable task7 = ()->{
            while (stop.get()){
                taskFactory.accessTask(agvs.get(6));
                agvs.get(6).run();
            }
        };
        executorService7.submit(task7);

        ExecutorService executorService8 = Executors.newSingleThreadExecutor();
        Runnable task8 = ()->{
            while (stop.get()){
                taskFactory.accessTask(agvs.get(7));
                agvs.get(7).run();
            }
        };
        executorService8.submit(task8);

        while (true){
            long count = tasks.stream().filter(p -> p.getState() == Task.COMPLETED).count();
            System.out.println("当前完成任务数："+count);
            if (count >= 500){
                stop.compareAndSet(true,false);
                System.out.println("stop:"+stop.get());
                closeThread(executorService1);
                closeThread(executorService2);
                closeThread(executorService3);
                closeThread(executorService4);
                closeThread(executorService5);
                closeThread(executorService6);
                closeThread(executorService7);
                closeThread(executorService8);
                break;
            }
        }
        double time = 0;
        for (Task task : TaskFactory.tasks) {
            time = time + task.getCompletedTime();
        }
        System.out.println("完成任务总共花费时间(h):"+time);
        for (int i = 0; i < 8; i++) {
            System.out.printf("%d号AGV完成任务数：%d\n",agvs.get(i).getId(),agvs.get(i).getCompletedTask());
            System.out.printf("%d号AGV行走距离(km)：%f\n",agvs.get(i).getId(),agvs.get(i).getDistance());
        }
    }

    /**
     * 关闭线程
     * @param executorService
     */
    public static void closeThread(ExecutorService executorService){
        try {
            System.out.println("尝试关闭ExecutorService");
            executorService.shutdown();
            //指定一段时间温和关闭
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            System.out.println("任务中断...");
        }
        finally {
            if (!executorService.isTerminated()) {
                System.out.println("结束未完成的任务...");
            }
            executorService.shutdownNow();
            System.out.println("ExecutorService被停止...");
        }
    }
}

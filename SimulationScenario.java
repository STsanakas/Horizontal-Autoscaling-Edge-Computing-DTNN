/*
 * CloudSim Plus: A modern, highly-extensible and easier-to-use Framework for
 * Modeling and Simulation of Cloud Computing Infrastructures and Services.
 * http://cloudsimplus.org
 *
 *     Copyright (C) 2015-2018 Universidade da Beira Interior (UBI, Portugal) and
 *     the Instituto Federal de Educação Ciência e Tecnologia do Tocantins (IFTO, Brazil).
 *
 *     This file is part of CloudSim Plus.
 *
 *     CloudSim Plus is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     CloudSim Plus is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with CloudSim Plus. If not, see <http://www.gnu.org/licenses/>.
 */
package org.cloudsimplus.examples.resourceusage;

import org.cloudbus.cloudsim.hosts.HostSimple;
import org.cloudsimplus.faultinjection.HostFaultInjection;
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerBestFit;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerFirstFit;

import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSmart;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerAbstract;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletSimple;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.Simulation;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.distributions.ContinuousDistribution;
import org.cloudbus.cloudsim.distributions.NormalDistr;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.HostSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.ResourceProvisionerSimple;
import org.cloudbus.cloudsim.resources.*;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerSpaceShared;

import org.cloudbus.cloudsim.utilizationmodels.UtilizationModel;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelDynamic;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelFull;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelStochastic;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.vms.VmSimple;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;
import org.cloudsimplus.listeners.EventInfo;
import org.cloudsimplus.listeners.EventListener;
import org.apache.commons.math3.distribution.NormalDistribution;

import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletSimple;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.Simulation;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.distributions.ContinuousDistribution;
import org.cloudbus.cloudsim.distributions.UniformDistr;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.HostSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.ResourceProvisionerSimple;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModel;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelFull;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.vms.VmSimple;
import org.cloudsimplus.autoscaling.HorizontalVmScaling;
import org.cloudsimplus.autoscaling.HorizontalVmScalingSimple;
import org.cloudsimplus.builders.tables.CloudletsTableBuilder;
import org.cloudsimplus.listeners.EventInfo;
import org.cloudsimplus.listeners.EventListener;
import org.cloudbus.cloudsim.core.CloudSimTags ;




import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparingDouble;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * Shows how to use the {@link Simulation#addOnClockTickListener(EventListener) onClockTick Listener}
 * to keep track os simulation clock and store VM's RAM and BW utilization along the time.
 * CloudSim Plus already has built-in features to obtain VM's CPU utilization.
 * Check {@link org.cloudsimplus.examples.power.PowerExample}.
 *
 * <p>The example uses the CloudSim Plus {@link EventListener} feature
 * to enable monitoring the simulation and dynamically collect RAM and BW usage.
 * It relies on
 * <a href="https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html">Java 8 Method References</a>
 * to set a method to be called for {@link Simulation#addOnClockTickListener(EventListener) onClockTick events}.
 * It enables getting notifications when the simulation clock advances, then creating and submitting new cloudlets.
 * </p>
 *
 * @author Manoel Campos da Silva Filho
 * @since CloudSim Plus 4.1.2
 *
 * @see VmsCpuUsageExample
 * @see org.cloudsimplus.examples.power.PowerExample
 */
public class SimulationScenario {
    /**
     * @see Datacenter#getSchedulingInterval()
     */
	private static final int SCHEDULING_INTERVAL = 1;
	private int TIME_TO_TERMINATE_SIMULATION = 3600*24;
    /**
     * The interval to request the creation of new Cloudlets.
     */
  //  private static final int CLOUDLETS_CREATION_INTERVAL = SCHEDULING_INTERVAL * 2;
    private int createsVms=0;
    private static final int HOSTS = 20;
    private static final int HOST_PES = 4;
    private static final int VMS = 5;
    private static final int CLOUDLETS = 0;
    private static final int CLOUDLET_PES = 4;
    //private static final int CLOUDLET_LENGTH = 30000;
	private static final String COMMA_DELIMITER = null;
	private static final long[] CLOUDLET_LENGTHS = {500, 1000, 1500};
    private ContinuousDistribution rand;
    private final CloudSim simulation;
    private DatacenterBroker broker0;
    private Host host0;
    private List<Host> hostList;
    private List<Vm> vmList;
    private List<Cloudlet> cloudletList;
    private List<Vm> vmcrList;
    private List<Vm> vmcreList;

    private boolean vmDestructionRequested;
    /**
     * 
     * 
     * Different lengths that will be randomly assigned to created Cloudlets.
     */
    
   // ArrayList<Double> cpuus = new ArrayList<Double>();
    
    private int timeCheck = 0;
    private int timeCheckp = -2;
    private int createdCloudlets;
    private int timeCheckd= -3;
    private int timeChecku= -1;
    private int timeCheckus= 0;
    private int timeCheckusd= 0;
    private double cloudletdelay=0;
    
    private int timeCheckcpu= -3;
    private int timeCheckram= -3;
    private int timeCheckbw= -3;

    private int vmrun =0;
    private double totcpu=0.00;
    private int desvm=0;
    private int vmdif=0;
    private double[] cpuus = new double [20];
    private double[] ramus = new double [20];
    private double[] bwus = new double [20];
    private int[] cloudletvm = new int [20];
    private double clus = 0.0;
    private double[][][] mega = new double[20][6][20];
    private int[] warn= new int [20];
    private double utex=0.00;

    private final Map<Vm, Map<Double, Double>> allVmsRamUtilizationHistory;

    /** @see #allVmsRamUtilizationHistory */
    private final Map<Vm, Map<Double, Double>> allVmsBwUtilizationHistory;

    private final Map<Vm, Map<Double, Double>> allVmsCpuUtilizationHistory;
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        new SimulationScenario();
    }
    private SimulationScenario() throws FileNotFoundException, IOException {
        /*Enables just some level of log messages.
          Make sure to import org.cloudsimplus.util.Log;*/
        //Log.setLevel(ch.qos.logback.classic.Level.WARN);
  
   
        final long seed = 1;
        rand = new UniformDistr(0, CLOUDLET_LENGTHS.length, seed);
        hostList = new ArrayList<>(HOSTS);
        vmList = new ArrayList<>(VMS);
        cloudletList = new ArrayList<>(CLOUDLETS);
        vmcrList = new ArrayList<>(VMS);
        vmcreList = new ArrayList<>(VMS);


        simulation = new CloudSim();
        simulation.terminateAt(TIME_TO_TERMINATE_SIMULATION);

       simulation.addOnClockTickListener(info -> {
			try {
				onClockTickListener1(info);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
        simulation.addOnClockTickListener(this::createNewCloudlets);
        simulation.addOnClockTickListener(this::findtask);
        simulation.addOnClockTickListener(this::scaletotal);
        simulation.addOnClockTickListener(this::cpuusagebymin);
        simulation.addOnClockTickListener(this::ramusagebymin);
        simulation.addOnClockTickListener(this::bwusagebymin);
        simulation.addOnClockTickListener(this::minreset);

        
  


        

        

        createDatacenter();
        broker0 = new DatacenterBrokerSimple(simulation);

        /*
         * Defines the Vm Destruction Delay Function as a lambda expression
         * so that the broker will wait 10 seconds before destroying an idle VM.
         * By commenting this line, no down scaling will be performed
         * and idle VMs will be destroyed just after all running Cloudlets
         * are finished and there is no waiting Cloudlet. */
        //broker0.setVmDestructionDelayFunction(vm -> 60.0);

        //broker0.setVmDestructionDelayFunction(vm -> 60.00);

        //vmcrList.addAll(createListOfScalableVms(VMS));

        createCloudletList();

        //createAndSubmitCloudlets(2);      
        

        //broker0.submitVmList(vmcrList);
        broker0.submitVm(createVm());
        broker0.submitVm(createVm());
        broker0.submitVm(createVm());
        broker0.submitVm(createVm());
        broker0.submitVm(createVm());
       
        
       
        
        
        


        //broker0.submitCloudletList(cloudletList);
        
        
        allVmsRamUtilizationHistory = initializeUtilizationHistory();
        allVmsBwUtilizationHistory = initializeUtilizationHistory();
        allVmsCpuUtilizationHistory = initializeUtilizationHistory();
        
        
        
        simulation.start();
        final List<Cloudlet> finishedCloudlets = broker0.getCloudletFinishedList();
        new CloudletsTableBuilder(finishedCloudlets).build();
        //printVmListResourceUtilizationHistory();
        //printSimulationResults();
       // System.out.println(this.hostList + " hosts" );
        //System.out.println(this.vmcreList + " vms" );
        //System.out.println(this.cloudletList + " cloudlets" );
    	//System.out.println(getClass().getSimpleName() + " finished!" );
        kath();
        metr();
    }
    private void printVmListResourceUtilizationHistory() {
        System.out.println();
        for (Vm vm : vmList) {
            printVmUtilizationHistory(vm);
        }
    }
    
    private void kath() throws UnsupportedEncodingException, FileNotFoundException, IOException {
    	int dap=0;
    	int dapa=0;
    	int yp=0;
    	double dapap=0.00;
    	double pipip=0.00;
    	List<Cloudlet> finishedCloudlets = broker0.getCloudletFinishedList();
    	for (Cloudlet cloudlet: finishedCloudlets) {
    		yp++;
    		cloudletdelay = cloudletdelay + cloudlet.getWaitingTime();
    		pipip=cloudlet.getExecStartTime()-cloudlet.getLastDatacenterArrivalTime();
    		dapap=dapap + pipip;
    		if(cloudlet.getWaitingTime()<1 && cloudlet.getLength()==500){
    			dap= dap+1;
    		}
    		if(cloudlet.getWaitingTime()<1 && cloudlet.getLength()==1500){
    			dapa= dapa+1;
    		}
    		
    	}
        System.out.println(this.cloudletdelay);
        System.out.println(dap);
        System.out.println(dapa);
        System.out.println(dapap);
        System.out.println(dapap/broker0.getCloudletFinishedList().size());
        
        

    	}
    
    private void metr() throws IOException, FileNotFoundException, IOException {
    	List<Cloudlet> finishedCloudlets = broker0.getCloudletFinishedList();
    	
    	try (BufferedWriter writer2 = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("erg/dataproper.csv", true), "UTF-8"))) {
    		
    	for (Cloudlet cloudlet: finishedCloudlets) {
    		
        			 writer2.write(String.valueOf(cloudlet.getId()));
                     writer2.write(",");
                    writer2.write(String.valueOf(cloudlet.getLastDatacenterArrivalTime()));
                    writer2.write(",");
                    writer2.write(String.valueOf(cloudlet.getWaitingTime()));
                    writer2.write(",");
                    writer2.write(String.valueOf(cloudlet.getExecStartTime()));
                    writer2.write(",");
                    writer2.write(String.valueOf(cloudlet.getFinishTime()));
                    writer2.write(",");
                    writer2.write(String.valueOf(cloudlet.getLength()));
                    writer2.write(",");
                    writer2.write(String.valueOf(cloudlet.getFinishTime()-cloudlet.getLastDatacenterArrivalTime()));
                    writer2.newLine();
        		}
    						
    					 	}
    		
    	}
    
    
    


	private void printSimulationResults() {
	    List<Cloudlet> finishedCloudlets = broker0.getCloudletFinishedList();
	    Comparator<Cloudlet> sortByVmId = comparingDouble(c -> c.getVm().getId());
	    Comparator<Cloudlet> sortByStartTime = comparingDouble(c -> c.getExecStartTime());
	    finishedCloudlets.sort(sortByVmId.thenComparing(sortByStartTime));
        new CloudletsTableBuilder(finishedCloudlets).build();

	}
  
    /**
     * Prints the RAM and BW utilization history of a given Vm.
     */
    private void printVmUtilizationHistory(Vm vm) {
        System.out.println(vm + " RAM, BW and CPU utilization history");
        System.out.println("----------------------------------------------------------------------------------");

        //A set containing all resource utilization collected times
        final Set<Double> timeSet = allVmsRamUtilizationHistory.get(vm).keySet();

        final Map<Double, Double> vmRamUtilization = allVmsRamUtilizationHistory.get(vm);
        final Map<Double, Double> vmBwUtilization = allVmsBwUtilizationHistory.get(vm);
        final Map<Double, Double> vmCpuUtilization = allVmsCpuUtilizationHistory.get(vm);

        for (final double time : timeSet) {
            System.out.printf(
                "Time: %10.1f secs | RAM: %10.2f%% | BW: %10.2f%% | CPU: %10.2f%%%n",
                time, vmRamUtilization.get(time) * 100, vmBwUtilization.get(time) * 100, vmCpuUtilization.get(time) * 100);
        }
        System.out.printf("----------------------------------------------------------------------------------%n%n");
    }

    private void RunPython(String script) {
    	String s = null;
    	try {
            System.out.println("Going to Python");
	    // run the Unix "ps -ef" command
            // using the Runtime exec method:
            Process p = Runtime.getRuntime().exec("python "+script);
            
            BufferedReader stdInput = new BufferedReader(new 
                 InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new 
                 InputStreamReader(p.getErrorStream()));

            // read the output from the command
            //System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }
            System.out.println("Back Python");
            //System.exit(0);
        }
        catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Initializes a map that will store utilization history for
     * some resource (such as RAM or BW) of every VM.
     * It also creates an empty internal map to store
     * the resource utilization for every VM along the simulation execution.
     * The internal map for every VM will be empty.
     * They are filled inside the {@link #onClockTickListener1(EventInfo)}.
     */
    private Map<Vm, Map<Double, Double>> initializeUtilizationHistory() {
        //TreeMap sorts entries based on the key
        final Map<Vm, Map<Double, Double>> map = new HashMap<>();

        for (Vm vm : vmList) {
            map.put(vm, new TreeMap<>());
        }

        return map;
    }

    /**
     * Keeps track of simulation clock.
     * Every time the clock changes, this method is called.
     * To enable this method to be called at a defined
     * interval, you need to set the {@link Datacenter#setSchedulingInterval(double) scheduling interval}.
     *
     * @param evt information about the clock tick event
     * @throws IOException 
     * @see #SCHEDULING_INTERVAL
     */
 
    
    private void onClockTickListener1(EventInfo eventInfo) throws IOException {
    	final int time = (int) eventInfo.getTime();
        if (time + 1>timeCheckp) {
            timeCheckp = timeCheckp + 60;

        	collectVmResourceUtilization(this.allVmsRamUtilizationHistory, Ram.class);
        	collectVmResourceUtilization(this.allVmsBwUtilizationHistory, Bandwidth.class);
        	collectVmCpuResourceUtilization(this.allVmsCpuUtilizationHistory);
        	graps();
        	
        	predict(70.00);
           // decide(allVmsRamUtilizationHistory, allVmsBwUtilizationHistory, allVmsCpuUtilizationHistory);
           //scaledown();
        }
    }
        

    /**
     * Collects the utilization percentage of a given VM resource for every VM.
     * CloudSim Plus already has built-in features to obtain VM's CPU utilization.
     * Check {@link org.cloudsimplus.examples.power.PowerExample}.
     *
     * @param allVmsUtilizationHistory the map where the collected utilization for every VM will be stored
     * @param resourceClass the kind of resource to collect its utilization (usually {@link Ram} or {@link Bandwidth}).
     */
    private void collectVmResourceUtilization(final Map<Vm, Map<Double, Double>> allVmsUtilizationHistory, Class<? extends ResourceManageable> resourceClass) {
        for (Vm vm : vmList) {
            /*Gets the internal resource utilization map for the current VM.
            * The key of this map is the time the usage was collected (in seconds)
            * and the value the percentage of utilization (from 0 to 1). */
            //final Map<Double, Double> vmUtilizationHistory = allVmsUtilizationHistory.get(vm);
            //vmUtilizationHistory.put(simulation.clock(), vm.getResource(resourceClass).getPercentUtilization());
        }
    }
    private void collectVmCpuResourceUtilization(final Map<Vm, Map<Double, Double>> allVmsUtilizationHistory) {
        for (Vm vm : vmList) {       		        	
           // final Map<Double, Double> vmUtilizationHistory = allVmsUtilizationHistory.get(vm);
            //vmUtilizationHistory.put(simulation.clock(), vm.getCpuPercentUtilization());
        }
    }
    
    private void clbymin(EventInfo eventInfo) {
    	final int time = (int) eventInfo.getTime();
    	
    	if (time % 1 == 0 && time + 1 > timeCheckcpu){
            timeCheckcpu = timeCheckcpu + 1;
            for (Vm vm : vmcreList) {
            	
            	//cpuus.set((int) vm.getId(), (cpuus.get((int) vm.getId())+ vm.getCpuPercentUtilization()));
            	cpuus[(int) vm.getId()] = cpuus[(int) vm.getId()] + vm.getCpuPercentUtilization();
            }
    	}
    }
    
    private void cpuusagebymin(EventInfo eventInfo) {
    	final int time = (int) eventInfo.getTime();
    	
    	if (time % 1 == 0 && time + 1 > timeCheckcpu){
            timeCheckcpu = timeCheckcpu + 1;
            for (Vm vm : vmcreList) {
            	
            	//cpuus.set((int) vm.getId(), (cpuus.get((int) vm.getId())+ vm.getCpuPercentUtilization()));
            	cpuus[(int) vm.getId()] = cpuus[(int) vm.getId()] + vm.getCpuPercentUtilization();
            }
    	}
    }
    
    private void ramusagebymin(EventInfo eventInfo) {
    	final int time = (int) eventInfo.getTime();
    	
    	if (time % 1 == 0 && time + 1 > timeCheckram){
            timeCheckram = timeCheckram + 1;
            for (Vm vm : vmcreList) {
            	
            	//cpuus.set((int) vm.getId(), (cpuus.get((int) vm.getId())+ vm.getCpuPercentUtilization()));
            	ramus[(int) vm.getId()] = ramus[(int) vm.getId()] + vm.getResource(Ram.class).getPercentUtilization();
            }
    	}
    }
    
    private void bwusagebymin(EventInfo eventInfo) {
    	final int time = (int) eventInfo.getTime();
    	
    	if (time % 1 == 0 && time + 1 > timeCheckbw){
            timeCheckbw = timeCheckbw + 1;
            for (Vm vm : vmcreList) {
            	
            	//cpuus.set((int) vm.getId(), (cpuus.get((int) vm.getId())+ vm.getCpuPercentUtilization()));
            	bwus[(int) vm.getId()] = bwus[(int) vm.getId()] + vm.getResource(Bandwidth.class).getPercentUtilization();
            }
    	}
    }
    
    
    
    
    private void minreset(EventInfo eventInfo) {
    	final int time = (int) eventInfo.getTime();

    	if (time +1 >timeCheckusd){
            timeCheckusd = timeCheckusd + 60;
            System.out.print("XXXXXXXXXXXXXXXX");
            for (int i=0;i<20;i++) {
            	//cpuus.set((int) vm.getId(), 0.00);
            	ramus[i] = 0;
            	cpuus[i] = 0;
            	bwus[i] = 0;
            	cloudletvm[i]=0;

            	
            }
    	}
    }
    	
 
    private void findtask(EventInfo eventInfo) {
    	final int time = (int) eventInfo.getTime();
    	 if (time +1 >timeCheckd){
         	timeCheckd = timeCheckd + 60;
    	List<Cloudlet> finishedCloudlets = broker0.getCloudletFinishedList();
            for(Cloudlet cloudlet : finishedCloudlets) {
            	if(time - cloudlet.getLastDatacenterArrivalTime()<60 ) {
            	cloudletvm[(int) cloudlet.getVm().getId()]++;
            	}
    		}
    	 }
    }
            
           
    	
  
    
    
    
    private void scaletotal(EventInfo eventInfo) {
    	
    	final int time = (int) eventInfo.getTime();
    	System.out.print(time);
        if (time +1 >timeChecku){
        	System.out.print("AAAAAAAAAAAAA");
        	System.out.print(cpuus[0]/59.00);
        	System.out.print("AAAAAAAAAAAAA");
        	timeChecku = timeChecku + 60;
            vmcrList.clear();
            vmcrList.addAll(vmcreList);
        	
            
            for (Vm vm : vmcrList) {
            	
            		//smart
                	//if(warn[(int) vm.getId()]==0  && createsVms >5 && vm.getId() == createsVms-1 && cpuus[(int) vm.getId()] >0.04) {
                	
            		//baseline
                  if(cpuus[(int) vm.getId()]/59.00 < 0.4   && createsVms >5 && vm.getId() == createsVms-1 && cpuus[(int) vm.getId()] >0.04) {
                    	
                    
            		
            	  final List<Cloudlet> affected = broker0.destroyVm(vmcreList.get((int)(createsVms-1)));
            	  
            		createsVms--;
            		vmcreList.remove(createsVms);
            		vmList.remove(createsVms);

            	
                   
                   
            		broker0.submitCloudletList(affected);
            	}
    		
            		//smart
            		if(warn[(int) vm.getId()]==2  && createsVms<20 && time>1200.00) {
			//baseline
            		if(cpuus[(int) vm.getId()]/59.00 > 0.80 && createsVms<20 && time>1200.00) {
            			System.out.print("cpuus[(int) vm.getId()]/59.00");
                    //vmcrList.add(vmnew);
            				
                broker0.submitVm(createVm());
    		}
            	
        }	
            	
            	
            }}
    	
    private void scaletotalk(EventInfo eventInfo) {

    	final int time = (int) eventInfo.getTime();
        if (time +1 >timeChecku){
        	timeChecku = timeChecku + 60;
            vmcrList.clear();
            vmcrList.addAll(vmcreList);
        	
            totcpu=0.00;
            vmrun=0;
            vmdif=0;
            
            for(int j=0;j<20;j++) {
	    		 if((100*cpuus[j]/59.00)>1) {
	    			 vmrun++;
	    		 }
	    	 }
            
            for(int i=0;i<20;i++) {
            	totcpu= (100*cpuus[i]/59.00) + totcpu;
            }
        	totcpu = totcpu/vmrun;
        	
            
        	
         
            desvm = (int) Math.ceil((vmrun*(totcpu/50.00)));
            vmdif=desvm-vmrun;	
            
           //k8s
           if(vmdif<0) {
            		
        	   	while(vmdif<0 && createsVms >5 && cpuus[createsVms-1]>0.04) {
        	   		final List<Cloudlet> affected = broker0.destroyVm(vmcreList.get((int)(createsVms-1)));
            	  
            		createsVms--;
            		vmcreList.remove(createsVms);
            		vmList.remove(createsVms);
            		vmdif++;
            	
                   
                   
            		broker0.submitCloudletList(affected);
            	}
    		
           }	
            if(vmdif>0) {
            	
            	while(vmdif>0 && createsVms<20 && time>1200.00) {
            				
                broker0.submitVm(createVm());
                vmdif--;
    		}
            	
        }	
            	
            	
            }}
        
    
    private void scaletotalkk(EventInfo eventInfo) {

    	final int time = (int) eventInfo.getTime();
        if (time +1 >timeChecku){
        	timeChecku = timeChecku + 60;
            vmcrList.clear();
            vmcrList.addAll(vmcreList);
        	
            vmrun=0;
            
            for(int j=0;j<20;j++) {
	    		 if((100*cpuus[j]/59.00)>1) {
	    			 vmrun++;
	    		 }
	    	 }
            for (Vm vm : vmcrList) {
            	
            	 

            	if(cpuus[(int) vm.getId()]/59.00  < 0.3 && createsVms >5 && vm.getId() == createsVms-1 && cpuus[(int) vm.getId()] >0.04) {
            		
            		 desvm = (int) Math.ceil((vmrun*((100*cpuus[(int)vm.getId()]/59.00)/30.00)));
                     vmdif=desvm-vmrun;	
                     
             	   	while(vmdif<0 && createsVms >5 && cpuus[createsVms-1]>0.04) {
             	   	final List<Cloudlet> affected = broker0.destroyVm(vmcreList.get((int)(createsVms-1)));
                 		createsVms--;
                 		vmcreList.remove(createsVms);
                 		vmList.remove(createsVms);
                 		vmdif++;
                 	
                        
                        
                 		broker0.submitCloudletList(affected);
                 	}
         		
                }	
            	if(cpuus[(int) vm.getId()]/59.00 > 0.7 && createsVms<20 && time>1200.00) {
            		desvm = (int) Math.ceil((vmrun*((100*cpuus[(int)vm.getId()]/59.00)/70.00)));
                    vmdif=desvm-vmrun;	
                 	while(vmdif>0 && createsVms<20 && time>1200.00) {
                 				
                     broker0.submitVm(createVm());
                     vmdif--;
         		}
                 	
             }	
                 	
                 	
                 }}}
    
    
    
    
    
   private void decide (Map<Vm, Map<Double, Double>> map, Map<Vm, Map<Double, Double>> map2, Map<Vm, Map<Double, Double>> map3) {
    	if (createsVms < 20) {
    	//createHorizontalVmScaling(createVm());	    		
    	map.put(vmList.get(vmList.size() - 1) , new TreeMap<>());
    	map2.put(vmList.get(vmList.size() - 1) , new TreeMap<>());
    	map3.put(vmList.get(vmList.size() - 1) , new TreeMap<>());
    	System.out.println(broker0.getCloudletWaitingList());
    	//broker0.submitCloudletList(broker0.getCloudletWaitingList(), 0);
    	}    	
    }
    
   
    private void predict(Double threshold) throws IOException {
    		
    		    FileWriter csvWriter2 = new FileWriter("erg/dataset.csv", false);
		    	BufferedWriter bw = new BufferedWriter(csvWriter2);
		    	vmrun=5;
		    	for(int j=5;j<20;j++) {
		   		 if(100*cpuus[j]/59.00>1) {
		   			 vmrun++;
		   		 }
		    	}
    		    for(int i=0;i<20;i++) {
    		    	
    		    	bw.append(String.valueOf(i));
    		    	bw.append(",");
    		    	bw.append(String.valueOf(round(simulation.clock(),1)));
    		    	bw.append(",");
    		    	bw.append(String.valueOf(round(100*cpuus[i]/59.00,2)));
    		    	bw.append(",");
    		    	bw.append(String.valueOf(round(100*ramus[i]/59.00,2)));
    		    	bw.append(",");
    		    	bw.append(String.valueOf(round(100*bwus[i]/59.00,2)));
    		    	bw.append(",");
    		    	bw.append(String.valueOf(round(cloudletvm[i],2)));
    		    	bw.append(",");
    		    	bw.append(String.valueOf(vmrun));
    		    	bw.newLine();
    		    }	    		    	
    		    bw.close();
    		    //csvWriter2.flush();
    		    
    		    for(int i=0;i<20;i++) {
        		    for(int j=0;j<6;j++) {
            		    for(int k=0;k<19;k++) {

            		    	mega[k][j][i]=mega[k+1][j][i];
            		    }
        		    }
    		    }
    		    
    		    
    		    for(int i=0;i<20;i++) {
    		    		mega[19][0][i]=i;
        		    	mega[19][1][i]=round(simulation.clock(),1);
        		    	mega[19][2][i]=round(cpuus[i],2);
        		    	mega[19][3][i]=round(ramus[i],2);
        		    	mega[19][4][i]=round(bwus[i],2);
        		    	mega[19][5][i]=round(cloudletvm[i],2);	
        		    }

    		    

    		    FileWriter csvWriter3 = new FileWriter("erg/vm0.csv", false);
		    	BufferedWriter bw2 = new BufferedWriter(csvWriter3);

    		    for(int i=0;i<20;i++) {
    		    	
    		    	bw2.append(String.valueOf(0));
    		    	bw2.append(",");
    		    	bw2.append(String.valueOf(round(mega[i][1][0], 1)));
    		    	bw2.append(",");
    		    	bw2.append(String.valueOf(round(100*mega[i][2][0]/59.00, 2)));
    		    	bw2.append(",");
    		    	bw2.append(String.valueOf(round(100*mega[i][3][0]/59.00, 2)));
    		    	bw2.append(",");
    		    	bw2.append(String.valueOf(round(100*mega[i][4][0]/59.00, 2)));
    		    	bw2.append(",");
    		    	bw2.append(String.valueOf(round(mega[i][5][0], 2)));
    		    	bw2.append(",");
    		    	bw2.append(String.valueOf(vmrun));
    		    	bw2.newLine();
    		    }	    		    	
    		    bw2.close();
    		   
    		    
    		    FileWriter csvWriter4 = new FileWriter("erg/vm1.csv", false);
		    	BufferedWriter bw4 = new BufferedWriter(csvWriter4);

    		    for(int i=0;i<20;i++) {
    		    	
    		    	bw4.append(String.valueOf(1));
    		    	bw4.append(",");
    		    	bw4.append(String.valueOf(round(mega[i][1][1], 1)));
    		    	bw4.append(",");
    		    	bw4.append(String.valueOf(round(100*mega[i][2][1]/59.00, 2)));
    		    	bw4.append(",");
    		    	bw4.append(String.valueOf(round(100*mega[i][3][1]/59.00, 2)));
    		    	bw4.append(",");
    		    	bw4.append(String.valueOf(round(100*mega[i][4][1]/59.00, 2)));
    		    	bw4.append(",");
    		    	bw4.append(String.valueOf(round(mega[i][5][1], 2)));
    		    	bw4.append(",");
    		    	bw4.append(String.valueOf(vmrun));
    		    	bw4.newLine();
    		    }	    		    	
    		    bw4.close();
    		    
    		    FileWriter csvWriter5 = new FileWriter("erg/vm2.csv", false);
		    	BufferedWriter bw5 = new BufferedWriter(csvWriter5);

    		    for(int i=0;i<20;i++) {
    		    	
    		    	bw5.append(String.valueOf(2));
    		    	bw5.append(",");
    		    	bw5.append(String.valueOf(round(mega[i][1][2], 1)));
    		    	bw5.append(",");
    		    	bw5.append(String.valueOf(round(100*mega[i][2][2], 2)));
    		    	bw5.append(",");
    		    	bw5.append(String.valueOf(round(100*mega[i][3][2]/59.00, 2)));
    		    	bw5.append(",");
    		    	bw5.append(String.valueOf(round(100*mega[i][4][2]/59.00, 2)));
    		    	bw5.append(",");
    		    	bw5.append(String.valueOf(round(mega[i][5][2], 2)));
    		    	bw5.append(",");
    		    	bw5.append(String.valueOf(vmrun));
    		    	bw5.newLine();
    		    }	    		    	
    		    bw5.close();
    		    
    		    FileWriter csvWriter6 = new FileWriter("erg/vm3.csv", false);
		    	BufferedWriter bw6 = new BufferedWriter(csvWriter6);

    		    for(int i=0;i<20;i++) {
    		    	
    		    	bw6.append(String.valueOf(3));
    		    	bw6.append(",");
    		    	bw6.append(String.valueOf(round(mega[i][1][3], 1)));
    		    	bw6.append(",");
    		    	bw6.append(String.valueOf(round(100*mega[i][2][3]/59, 2)));
    		    	bw6.append(",");
    		    	bw6.append(String.valueOf(round(100*mega[i][3][3]/59, 2)));
    		    	bw6.append(",");
    		    	bw6.append(String.valueOf(round(100*mega[i][4][3]/59, 2)));
    		    	bw6.append(",");
    		    	bw6.append(String.valueOf(round(mega[i][5][3], 2)));
    		    	bw6.append(",");
    		    	bw6.append(String.valueOf(vmrun));
    		    	bw6.newLine();
    		    }	    		    	
    		    bw6.close();
    		    
    		    FileWriter csvWriter7 = new FileWriter("erg/vm4.csv", false);
		    	BufferedWriter bw7 = new BufferedWriter(csvWriter7);

    		    for(int i=0;i<20;i++) {
    		    	
    		    	bw7.append(String.valueOf(4));
    		    	bw7.append(",");
    		    	bw7.append(String.valueOf(round(mega[i][1][4], 1)));
    		    	bw7.append(",");
    		    	bw7.append(String.valueOf(round(100*mega[i][2][4]/59, 2)));
    		    	bw7.append(",");
    		    	bw7.append(String.valueOf(round(100*mega[i][3][4]/59, 2)));
    		    	bw7.append(",");
    		    	bw7.append(String.valueOf(round(100*mega[i][4][4]/59, 2)));
    		    	bw7.append(",");
    		    	bw7.append(String.valueOf(round(mega[i][5][4], 2)));
    		    	bw7.append(",");
    		    	bw7.append(String.valueOf(vmrun));
    		    	bw7.newLine();
    		    }	    		    	
    		    bw7.close();
    		    
    		    FileWriter csvWriter8 = new FileWriter("erg/vm5.csv", false);
		    	BufferedWriter bw8 = new BufferedWriter(csvWriter8);

    		    for(int i=0;i<20;i++) {
    		    	
    		    	bw8.append(String.valueOf(5));
    		    	bw8.append(",");
    		    	bw8.append(String.valueOf(round(mega[i][1][5], 1)));
    		    	bw8.append(",");
    		    	bw8.append(String.valueOf(round(100*mega[i][2][5], 2)));
    		    	bw8.append(",");
    		    	bw8.append(String.valueOf(round(100*mega[i][3][5], 2)));
    		    	bw8.append(",");
    		    	bw8.append(String.valueOf(round(100*mega[i][4][5], 2)));
    		    	bw8.append(",");
    		    	bw8.append(String.valueOf(round(mega[i][5][5], 2)));
    		    	bw8.append(",");
    		    	bw8.append(String.valueOf(vmrun));
    		    	bw8.newLine();
    		    }	    		    	
    		    bw8.close();
    		    
    		    FileWriter csvWriter9 = new FileWriter("erg/vm6.csv", false);
		    	BufferedWriter bw9 = new BufferedWriter(csvWriter9);

    		    for(int i=0;i<20;i++) {
    		    	
    		    	bw9.append(String.valueOf(6));
    		    	bw9.append(",");
    		    	bw9.append(String.valueOf(round(mega[i][1][6], 1)));
    		    	bw9.append(",");
    		    	bw9.append(String.valueOf(round(100*mega[i][2][6]/59.00, 2)));
    		    	bw9.append(",");
    		    	bw9.append(String.valueOf(round(100*mega[i][3][6]/59.00, 2)));
    		    	bw9.append(",");
    		    	bw9.append(String.valueOf(round(100*mega[i][4][6]/59.00, 2)));
    		    	bw9.append(",");
    		    	bw9.append(String.valueOf(round(mega[i][5][6], 2)));
    		    	bw9.append(",");
    		    	bw9.append(String.valueOf(vmrun));
    		    	bw9.newLine();
    		    }	    		    	
    		    bw9.close();
    		    
    		    FileWriter csvWriter10 = new FileWriter("erg/vm7.csv", false);
		    	BufferedWriter bw10 = new BufferedWriter(csvWriter10);

    		    for(int i=0;i<20;i++) {
    		    	
    		    	bw10.append(String.valueOf(7));
    		    	bw10.append(",");
    		    	bw10.append(String.valueOf(round(mega[i][1][7], 1)));
    		    	bw10.append(",");
    		    	bw10.append(String.valueOf(round(100*mega[i][2][7]/59.00, 2)));
    		    	bw10.append(",");
    		    	bw10.append(String.valueOf(round(100*mega[i][3][7]/59.00, 2)));
    		    	bw10.append(",");
    		    	bw10.append(String.valueOf(round(100*mega[i][4][7]/59.00, 2)));
    		    	bw10.append(",");
    		    	bw10.append(String.valueOf(round(mega[i][5][7], 2)));
    		    	bw10.append(",");
    		    	bw10.append(String.valueOf(vmrun));
    		    	bw10.newLine();
    		    }	    		    	
    		    bw10.close();
    		    
    		    FileWriter csvWriter11 = new FileWriter("erg/vm8.csv", false);
		    	BufferedWriter bw11 = new BufferedWriter(csvWriter11);

    		    for(int i=0;i<20;i++) {
    		    	
    		    	bw11.append(String.valueOf(8));
    		    	bw11.append(",");
    		    	bw11.append(String.valueOf(round(mega[i][1][8], 1)));
    		    	bw11.append(",");
    		    	bw11.append(String.valueOf(round(100*mega[i][2][8]/59.00, 2)));
    		    	bw11.append(",");
    		    	bw11.append(String.valueOf(round(100*mega[i][3][8]/59.00, 2)));
    		    	bw11.append(",");
    		    	bw11.append(String.valueOf(round(100*mega[i][4][8]/59.00, 2)));
    		    	bw11.append(",");
    		    	bw11.append(String.valueOf(round(mega[i][5][8], 2)));
    		    	bw11.append(",");
    		    	bw11.append(String.valueOf(vmrun));
    		    	bw11.newLine();
    		    }	    		    	
    		    bw11.close();
    		    
    		    FileWriter csvWriter12 = new FileWriter("erg/vm9.csv", false);
		    	BufferedWriter bw12 = new BufferedWriter(csvWriter12);

    		    for(int i=0;i<20;i++) {
    		    	
    		    	bw12.append(String.valueOf(9));
    		    	bw12.append(",");
    		    	bw12.append(String.valueOf(round(mega[i][1][9], 1)));
    		    	bw12.append(",");
    		    	bw12.append(String.valueOf(round(100*mega[i][2][9]/59.00, 2)));
    		    	bw12.append(",");
    		    	bw12.append(String.valueOf(round(100*mega[i][3][9]/59.00, 2)));
    		    	bw12.append(",");
    		    	bw12.append(String.valueOf(round(100*mega[i][4][9]/59.00, 2)));
    		    	bw12.append(",");
    		    	bw12.append(String.valueOf(round(mega[i][5][9], 2)));
    		    	bw12.append(",");
    		    	bw12.append(String.valueOf(vmrun));
    		    	bw12.newLine();
    		    }	    		    	
    		    bw12.close();
    		    
    		    FileWriter csvWriter13 = new FileWriter("erg/vm10.csv", false);
		    	BufferedWriter bw13 = new BufferedWriter(csvWriter13);

    		    for(int i=0;i<20;i++) {
    		    	
    		    	bw13.append(String.valueOf(10));
    		    	bw13.append(",");
    		    	bw13.append(String.valueOf(round(mega[i][1][10], 1)));
    		    	bw13.append(",");
    		    	bw13.append(String.valueOf(round(100*mega[i][2][10]/59.00, 2)));
    		    	bw13.append(",");
    		    	bw13.append(String.valueOf(round(100*mega[i][3][10]/59.00, 2)));
    		    	bw13.append(",");
    		    	bw13.append(String.valueOf(round(100*mega[i][4][10]/59.00, 2)));
    		    	bw13.append(",");
    		    	bw13.append(String.valueOf(round(mega[i][5][10], 2)));
    		    	bw13.append(",");
    		    	bw13.append(String.valueOf(vmrun));
    		    	bw13.newLine();
    		    }	    		    	
    		    bw13.close();
    		    
    		    
    		    FileWriter csvWriter14 = new FileWriter("erg/vm11.csv", false);
		    	BufferedWriter bw14 = new BufferedWriter(csvWriter14);

    		    for(int i=0;i<20;i++) {
    		    	
    		    	bw14.append(String.valueOf(11));
    		    	bw14.append(",");
    		    	bw14.append(String.valueOf(round(mega[i][1][11], 1)));
    		    	bw14.append(",");
    		    	bw14.append(String.valueOf(round(100*mega[i][2][11]/59.00, 2)));
    		    	bw14.append(",");
    		    	bw14.append(String.valueOf(round(100*mega[i][3][11]/59.00, 2)));
    		    	bw14.append(",");
    		    	bw14.append(String.valueOf(round(100*mega[i][4][11]/59.00, 2)));
    		    	bw14.append(",");
    		    	bw14.append(String.valueOf(round(mega[i][5][11], 2)));
    		    	bw14.append(",");
    		    	bw14.append(String.valueOf(vmrun));
    		    	bw14.newLine();
    		    }	    		    	
    		    bw14.close();
    		    
    		    FileWriter csvWriter15 = new FileWriter("erg/vm12.csv", false);
		    	BufferedWriter bw15 = new BufferedWriter(csvWriter15);

    		    for(int i=0;i<20;i++) {
    		    	
    		    	bw15.append(String.valueOf(12));
    		    	bw15.append(",");
    		    	bw15.append(String.valueOf(round(mega[i][1][12], 1)));
    		    	bw15.append(",");
    		    	bw15.append(String.valueOf(round(100*mega[i][2][12]/59.00, 2)));
    		    	bw15.append(",");
    		    	bw15.append(String.valueOf(round(100*mega[i][3][12]/59.00, 2)));
    		    	bw15.append(",");
    		    	bw15.append(String.valueOf(round(100*mega[i][4][12]/59.00, 2)));
    		    	bw15.append(",");
    		    	bw15.append(String.valueOf(round(mega[i][5][10], 2)));
    		    	bw15.append(",");
    		    	bw15.append(String.valueOf(vmrun));
    		    	bw15.newLine();
    		    }	    		    	
    		    bw15.close();
    		    
    		    FileWriter csvWriter16 = new FileWriter("erg/vm13.csv", false);
		    	BufferedWriter bw16 = new BufferedWriter(csvWriter16);

    		    for(int i=0;i<20;i++) {
    		    	
    		    	bw16.append(String.valueOf(13));
    		    	bw16.append(",");
    		    	bw16.append(String.valueOf(round(mega[i][1][13], 1)));
    		    	bw16.append(",");
    		    	bw16.append(String.valueOf(round(100*mega[i][2][13]/59.00, 2)));
    		    	bw16.append(",");
    		    	bw16.append(String.valueOf(round(100*mega[i][3][13]/59.00, 2)));
    		    	bw16.append(",");
    		    	bw16.append(String.valueOf(round(100*mega[i][4][13]/59.00, 2)));
    		    	bw16.append(",");
    		    	bw16.append(String.valueOf(round(mega[i][5][13], 2)));
    		    	bw16.append(",");
    		    	bw16.append(String.valueOf(vmrun));
    		    	bw16.newLine();
    		    }	    		    	
    		    bw16.close();
    		    
    		    FileWriter csvWriter17 = new FileWriter("erg/vm14.csv", false);
		    	BufferedWriter bw17 = new BufferedWriter(csvWriter17);

    		    for(int i=0;i<20;i++) {
    		    	
    		    	bw17.append(String.valueOf(14));
    		    	bw17.append(",");
    		    	bw17.append(String.valueOf(round(mega[i][1][14], 1)));
    		    	bw17.append(",");
    		    	bw17.append(String.valueOf(round(100*mega[i][2][14]/59.00, 2)));
    		    	bw17.append(",");
    		    	bw17.append(String.valueOf(round(100*mega[i][3][14]/59.00, 2)));
    		    	bw17.append(",");
    		    	bw17.append(String.valueOf(round(100*mega[i][4][14]/59.00, 2)));
    		    	bw17.append(",");
    		    	bw17.append(String.valueOf(round(mega[i][5][14], 2)));
    		    	bw17.append(",");
    		    	bw17.append(String.valueOf(vmrun));
    		    	bw17.newLine();
    		    }	    		    	
    		    bw17.close();
    		    
    		    FileWriter csvWriter18 = new FileWriter("erg/vm15.csv", false);
		    	BufferedWriter bw18 = new BufferedWriter(csvWriter18);

    		    for(int i=0;i<20;i++) {
    		    	
    		    	bw18.append(String.valueOf(15));
    		    	bw18.append(",");
    		    	bw18.append(String.valueOf(round(mega[i][1][15], 1)));
    		    	bw18.append(",");
    		    	bw18.append(String.valueOf(round(100*mega[i][2][15]/59.00, 2)));
    		    	bw18.append(",");
    		    	bw18.append(String.valueOf(round(100*mega[i][3][15]/59.00, 2)));
    		    	bw18.append(",");
    		    	bw18.append(String.valueOf(round(100*mega[i][4][15]/59.00, 2)));
    		    	bw18.append(",");
    		    	bw18.append(String.valueOf(round(mega[i][5][15], 2)));
    		    	bw18.append(",");
    		    	bw18.append(String.valueOf(vmrun));
    		    	bw18.newLine();
    		    }	    		    	
    		    bw18.close();
    		    
    		    FileWriter csvWriter19 = new FileWriter("erg/vm16.csv", false);
		    	BufferedWriter bw19 = new BufferedWriter(csvWriter19);

    		    for(int i=0;i<20;i++) {
    		    	
    		    	bw19.append(String.valueOf(16));
    		    	bw19.append(",");
    		    	bw19.append(String.valueOf(round(mega[i][1][16], 1)));
    		    	bw19.append(",");
    		    	bw19.append(String.valueOf(round(100*mega[i][2][16]/59.00, 2)));
    		    	bw19.append(",");
    		    	bw19.append(String.valueOf(round(100*mega[i][3][16]/59.00, 2)));
    		    	bw19.append(",");
    		    	bw19.append(String.valueOf(round(100*mega[i][4][16]/59.00, 2)));
    		    	bw19.append(",");
    		    	bw19.append(String.valueOf(round(mega[i][5][16], 2)));
    		    	bw19.append(",");
    		    	bw19.append(String.valueOf(vmrun));
    		    	bw19.newLine();
    		    }	    		    	
    		    bw19.close();
    		    
    		    FileWriter csvWriter20 = new FileWriter("erg/vm17.csv", false);
		    	BufferedWriter bw20 = new BufferedWriter(csvWriter20);

    		    for(int i=0;i<20;i++) {
    		    	
    		    	bw20.append(String.valueOf(17));
    		    	bw20.append(",");
    		    	bw20.append(String.valueOf(round(mega[i][1][17], 1)));
    		    	bw20.append(",");
    		    	bw20.append(String.valueOf(round(100*mega[i][2][17]/59.00, 2)));
    		    	bw20.append(",");
    		    	bw20.append(String.valueOf(round(100*mega[i][3][17]/59.00, 2)));
    		    	bw20.append(",");
    		    	bw20.append(String.valueOf(round(100*mega[i][4][17]/59.00, 2)));
    		    	bw20.append(",");
    		    	bw20.append(String.valueOf(round(mega[i][5][17], 2)));
    		    	bw20.append(",");
    		    	bw20.append(String.valueOf(vmrun));
    		    	bw20.newLine();
    		    }	    		    	
    		    bw20.close();
    		    
    		    FileWriter csvWriter21 = new FileWriter("erg/vm18.csv", false);
		    	BufferedWriter bw21 = new BufferedWriter(csvWriter21);

    		    for(int i=0;i<20;i++) {
    		    	
    		    	bw21.append(String.valueOf(18));
    		    	bw21.append(",");
    		    	bw21.append(String.valueOf(round(mega[i][1][18], 1)));
    		    	bw21.append(",");
    		    	bw21.append(String.valueOf(round(100*mega[i][2][18]/59.00, 2)));
    		    	bw21.append(",");
    		    	bw21.append(String.valueOf(round(100*mega[i][3][18]/59.00, 2)));
    		    	bw21.append(",");
    		    	bw21.append(String.valueOf(round(100*mega[i][4][18]/59.00, 2)));
    		    	bw21.append(",");
    		    	bw21.append(String.valueOf(round(mega[i][5][18], 2)));
    		    	bw21.append(",");
    		    	bw21.append(String.valueOf(vmrun));
    		    	bw21.newLine();
    		    }	    		    	
    		    bw21.close();
    		    
    		    FileWriter csvWriter22 = new FileWriter("erg/vm19.csv", false);
		    	BufferedWriter bw22 = new BufferedWriter(csvWriter22);

    		    for(int i=0;i<20;i++) {
    		    	
    		    	bw22.append(String.valueOf(19));
    		    	bw22.append(",");
    		    	bw22.append(String.valueOf(round(mega[i][1][19], 1)));
    		    	bw22.append(",");
    		    	bw22.append(String.valueOf(round(100*mega[i][2][19]/59.00, 2)));
    		    	bw22.append(",");
    		    	bw22.append(String.valueOf(round(100*mega[i][3][19]/59.00, 2)));
    		    	bw22.append(",");
    		    	bw22.append(String.valueOf(round(100*mega[i][4][19]/59.00, 2)));
    		    	bw22.append(",");
    		    	bw22.append(String.valueOf(round(mega[i][5][19], 2)));
    		    	bw22.append(",");
    		    	bw22.append(String.valueOf(vmrun));
    		    	bw22.newLine();
    		    }	    		    	
    		    bw22.close();
    		    
    	    	RunPython("erg/predicttt.py");
    	    	
    	    	try (BufferedReader br2 = new BufferedReader(new FileReader("erg/prediction.csv"))) {
    	    		String line;
            		List<Double> prediction = new ArrayList<>();
    	    		while ((line = br2.readLine()) != null) {
        		        String[] values = line.split(","); 
        		        int size = values.length;
       		     
        		        for(int i=0; i<size; i++) {
        		        	prediction.add(Double.parseDouble(values[i]));        		           
        		        }            		        
        		    }
    	    		
    	    		System.out.println(prediction);
    	    		for(int i=0;i<20;i++) { 
    	    			warn[i]=2;
    	    		}
    	    		
    	    		for(int i=0;i<20;i++) { 
    	    		Double CPU_PRED= prediction.get(i);
    	   
    	    		//vm.setCPU_STATUS("warning");
    	    		//vm.setRAM_STATUS("warning");
    	    		//vm.setBW_STATUS("warning");
    	    		if (CPU_PRED < 80) {
    	    			warn[i]=1;
    	    		}
    	    		//if (RAM_PRED < threshold) {
    	    		//	vm.setRAM_STATUS("ok");
    	    		//}
    	    		//if (BW_PRED < threshold) {
    	    		//	vm.setBW_STATUS("ok");
    	    		//}
    	    		if (CPU_PRED < 40) {
    	    			warn[i]=0;
    	    		}
    	    		//if (RAM_PRED < threshold - 40) {
    	    		//	vm.setRAM_STATUS("wl");
    	    		//}
    	    		//if (BW_PRED < threshold) {
    	    			//vm.setBW_STATUS("wl");
    	    		//}
    	    		//if (vm.getCPU_STATUS() == "warning") {
    	    			//printwarn(vm, "CPU");
    	    		//}
    	    		}
    	    		//if (vm.getRAM_STATUS() == "warning") {
    	    		//	printwarn(vm, "RAM");
    	    		//}
    	    		//if (vm.getBW_STATUS() == "warning") {
    	    			//printwarn(vm, "BW");
    	    		//}
    	    	} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
    	    	
    	    	
    
			
    	} 
    

    public void printwarn(Vm vm, String resource) {
		System.out.print("VM ");
		System.out.print(vm.getId());
		System.out.print(" ");
		System.out.print(resource);
		System.out.print(" warning at ");
		System.out.print(round(simulation.clock(),1));
		System.out.print("s, " );
		System.out.print(resource);
		System.out.print(" load:");
		if (resource== "BW") {
			System.out.print(round(100*vm.getResource(Bandwidth.class).getPercentUtilization(),2));
		}
		else if (resource== "RAM") {
			System.out.print(round(100*vm.getResource(Ram.class).getPercentUtilization()/60,2));
		}
		else {
			System.out.print(round(100*vm.getCpuPercentUtilization(),2));
		}		
		System.out.println("%");
    }
    
    public void graps() throws IOException {
    	utex=cpuus[0]/59.00;
    	vmrun=0;
    	try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("erg/clcp.csv", true), "UTF-8"))) {
    	

		    for(int i=0;i<20;i++) {
		    	
		    	writer.append(String.valueOf(i));
		    	writer.append(",");
		    	writer.append(String.valueOf(round(simulation.clock(),1)));
		    	writer.append(",");
		    	writer.append(String.valueOf(round(100*cpuus[i]/59.00,2)));
		    	writer.append(",");
		    	writer.append(String.valueOf(round(100*ramus[i]/59.00,2)));
		    	writer.append(",");
		    	writer.append(String.valueOf(round(100*bwus[i]/59.00,2)));
		    	writer.append(",");
		    	writer.append(String.valueOf(round(cloudletvm[i],2)));
		    	writer.append(",");
		    	 for(int j=0;j<20;j++) {
		    		 if(100*cpuus[j]/59.00>1) {
		    			 vmrun++;
		    		 }
		    	 }
		    	writer.append(String.valueOf(vmrun));
		    	vmrun=0;
		    	writer.newLine();
		    }	    		    	
		   //writer.close();
    		
    	}		
					 	
    	
   }
    
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    
    
    
    
    private void createNewCloudlets(EventInfo eventInfo) {
    	int clsum = 0;
        final int time = (int) eventInfo.getTime();
        if (time % 1 == 0 && timeCheck < time + 1) {
        	timeCheck = timeCheck + 1 ;
            List<Cloudlet> newCloudlets = new ArrayList<>();

        	if((time%86400)<43200) {
        		final int numCLP = ((time%43200)/15000);
        		 final int numberOfCloudlets = getPoissonRandom(numCLP);
        		 clsum = clsum + numberOfCloudlets;
        		System.out.printf("\t#Creating %d Cloudlets at time %d.%n", numberOfCloudlets, time);
                for (int i = 0; i < numberOfCloudlets; i++) {
                    Cloudlet cloudlet = createCloudlet();
                    cloudletList.add(cloudlet);
                    newCloudlets.add(cloudlet);
                }

        	}
        	if((time%86400>32400)&&time%86400<37800) {
        		final int numCLP = ((time%43200)/10000);
       		 final int numberOfCloudlets = getPoissonRandom(numCLP);
       		clsum = clsum + numberOfCloudlets;
       		System.out.printf("\t#Creating %d Cloudlets at time %d.%n", numberOfCloudlets, time);
               for (int i = 0; i < numberOfCloudlets; i++) {
                   Cloudlet cloudlet = createCloudlet();
                   cloudletList.add(cloudlet);
                   newCloudlets.add(cloudlet);
               }
        	}
        	if((time%86400>37800)&&time%86400<43200) {
        		final int numCLP = ((time%43200)/4000);
       		 final int numberOfCloudlets = getPoissonRandom(numCLP);
       		clsum = clsum + numberOfCloudlets;
       		System.out.printf("\t#Creating %d Cloudlets at time %d.%n", numberOfCloudlets, time);
               for (int i = 0; i < numberOfCloudlets; i++) {
                   Cloudlet cloudlet = createCloudlet();
                   cloudletList.add(cloudlet);
                   newCloudlets.add(cloudlet);
               }
        	}
        	
        	if((time%86400>43200)&&time%86400<48600) {
        		final int numCLP = ((86400-(time%86400))/4000);
       		 final int numberOfCloudlets = getPoissonRandom(numCLP);
       		clsum = clsum + numberOfCloudlets;
       		System.out.printf("\t#Creating %d Cloudlets at time %d.%n", numberOfCloudlets, time);
               for (int i = 0; i < numberOfCloudlets; i++) {
                   Cloudlet cloudlet = createCloudlet();
                   cloudletList.add(cloudlet);
                   newCloudlets.add(cloudlet);
               }
        	}
        	
        	if((time%86400>48600)&&time%86400<54000) {
        		final int numCLP = ((86400-(time%86400))/10000);
       		 final int numberOfCloudlets = getPoissonRandom(numCLP);
       		clsum = clsum + numberOfCloudlets;
       		System.out.printf("\t#Creating %d Cloudlets at time %d.%n", numberOfCloudlets, time);
               for (int i = 0; i < numberOfCloudlets; i++) {
                   Cloudlet cloudlet = createCloudlet();
                   cloudletList.add(cloudlet);
                   newCloudlets.add(cloudlet);
               }
        	}
        	
        	
        	
        	if((time%86400)>43200) {
        		final int numCLP = ((86400-(time%86400))/15000);
        		 final int numberOfCloudlets = getPoissonRandom(numCLP);
        		 clsum = clsum + numberOfCloudlets;
        		System.out.printf("\t#Creating %d Cloudlets at time %d.%n", numberOfCloudlets, time);
                for (int i = 0; i < numberOfCloudlets; i++) {
                    Cloudlet cloudlet = createCloudlet();
                    cloudletList.add(cloudlet);
                    newCloudlets.add(cloudlet);
                }

        	}
        	//Collections.sort(newCloudlets, new Sortbyroll());
        	//Collections.reverse(newCloudlets);
        	
        	broker0.submitCloudletList(newCloudlets);
        	newCloudlets.clear();
        }
   
        
    }
    
	/*
	 * private void createNewCloudlets(EventInfo eventInfo) { final int time = (int)
	 * eventInfo.getTime(); if (time % 1 == 0 && timeCheck < time + 1) { timeCheck =
	 * timeCheck + 1 ;
	 * 
	 * 
	 * final int numCLP = (int)
	 * (20*Math.exp(-((((time%86400)-43200)*((time%86400)-43200))/(2*3600*3600))));
	 * final int numberOfCloudlets = getPoissonRandom(numCLP);
	 * System.out.printf("\t#Creating %d Cloudlets at time %d.%n",
	 * numberOfCloudlets, time); List<Cloudlet> newCloudlets = new
	 * ArrayList<>(numberOfCloudlets); for (int i = 0; i < numberOfCloudlets; i++) {
	 * Cloudlet cloudlet = createCloudlet(); cloudletList.add(cloudlet);
	 * newCloudlets.add(cloudlet); } broker0.submitCloudletList(newCloudlets);
	 * 
	 * }}
	 */
    
    /**
     * Creates a Datacenter and its Hosts.
     */
    private void  createDatacenter() {
        for(int i = 0; i < HOSTS; i++) {
            Host host = createHost();
            hostList.add(host);
        }

        final Datacenter dc = new DatacenterSimple(simulation, hostList, new VmAllocationPolicySimple());
        dc.setSchedulingInterval(SCHEDULING_INTERVAL);
        //return dc;
    }
    

 
        	
        	
        	
        
    private Host createHost() {
        List<Pe> peList = new ArrayList<>(HOST_PES);
        //List of Host's CPUs (Processing Elements, PEs)
        for (int i = 0; i < HOST_PES; i++) {
            peList.add(new PeSimple(1000, new PeProvisionerSimple()));
        }

        final long ram = 1024; //in Megabytes
        final long bw = 10000; //in Megabits/s
        final long storage = 1000000; //in Megabytes
        Host host = new HostSimple(ram, bw, storage, peList);
        host
            .setRamProvisioner(new ResourceProvisionerSimple())
            .setBwProvisioner(new ResourceProvisionerSimple())
            .setVmScheduler(new VmSchedulerTimeShared());
        return host;
    }

    private List<Vm> createListOfScalableVms(final int numberOfVms) {
        List<Vm> newList = new ArrayList<>(numberOfVms);
        for (int i = 0; i < numberOfVms; i++) {
            Vm vm = createVm();
            //createHorizontalVmScaling(vm);
            newList.add(vm);
        }

        return newList;
    }

    /**
     * Creates a {@link HorizontalVmScaling} object for a given VM.
     *
     * @param vm the VM for which the Horizontal Scaling will be created
     * @see #createListOfScalableVms(int)
     */
    
    
    

    /**
     * Creates a VM with pre-defined configuration.
     *
     * @param id the VM id
     * @param broker the broker that will be submit the VM
     * @return the created VM
     *
     */
    
    private Vm createVm() {
        final int id = createsVms++;
       
        Vm vmnew = new VmSimple(id, 1000, 4)
                .setRam(1024).setBw(1000).setSize(10000)
                .setCloudletScheduler(new CloudletSchedulerSpaceShared());
        
        if(vmnew.getId()>5) {
        	vmnew.setSubmissionDelay(300.00);
        }
       vmcreList.add(vmnew);
       vmList.add(vmnew);
        //vmcreList.get(id).addOnUpdateProcessingListener(this::scaletotal);
        
        
        
        return vmnew;
        
    }
    
    /**
     * Creates cloudlets and submit them to the broker, applying
     * a submission delay for each one (simulating the dynamic cloudlet arrival).
     *
     * @param vm Vm to run the cloudlets to be created
     * @param submissionDelay the delay the broker has to include when submitting the Cloudlets
     *
     * @see #createCloudlet(int, Vm, DatacenterBroker)
     */
    private void createAndSubmitCloudlets(long submissionDelay) {
        int cloudletId = cloudletList.size();
        List<Cloudlet> list = new ArrayList<>(CLOUDLETS);
        for(int i = 0; i < CLOUDLETS; i++){
            Cloudlet cloudlet = createCloudlet();
            list.add(cloudlet);
        }        
        cloudletList.addAll(list);
        broker0.submitCloudletList(list, submissionDelay);
    }

    /**
     * Creates a Cloudlet with specific {@link UtilizationModel} for RAM, BW and CPU.
     * You can change the method to use any {@link UtilizationModel} you want.
     * @return
     */
    
    private void createCloudletList() {
        for (int i = 0; i < CLOUDLETS; i++) {
            cloudletList.add(createCloudlet());
        }
    }
    private Cloudlet createCloudlet() {
        final int id = createdCloudlets++;
        
        UtilizationModelDynamic ramUtilizationModel = new UtilizationModelDynamic(utex/1.50 + 0.10);
        UtilizationModelDynamic bwUtilizationModel = new UtilizationModelDynamic(utex/2.00 + 0.15);
        //randomly selects a length for the cloudlet
        
        final long length = CLOUDLET_LENGTHS[(int) rand.sample()];
        UtilizationModel utilization = new UtilizationModelFull();
        return new CloudletSimple(id, length, 4)
            .setFileSize(1024)
            .setOutputSize(1024)
            .setUtilizationModelCpu(utilization)
            .setUtilizationModelRam(ramUtilizationModel)
            .setUtilizationModelBw(bwUtilizationModel);
    }

    /**
     * Defines how the Cloudlet's utilization of RAM and BW will increase along the simulation time.
     * @return the updated utilization for RAM or BW
     * @see #createCloudlet()
     */
    private double utilizationUpdate(UtilizationModelDynamic utilizationModel) {
        return utilizationModel.getUtilization() + utilizationModel.getTimeSpan() * 0.1;
    }
    private static int getPoissonRandom(double mean) {
        Random r = new Random();
        double L = Math.exp(-mean);
        int k = 0;
        double p = 1.0;
        do {
            p = p * r.nextDouble();
            k++;
        } while (p > L);
        return k - 1;
    }
    class Sortbyroll implements Comparator<Cloudlet>
	{
	    // Used for sorting in ascending order of
	    // roll number
	    public int compare(Cloudlet a, Cloudlet b)
	    {
	        return (int) (a.getLength() - b.getLength());
	    }
	}
    
   
}
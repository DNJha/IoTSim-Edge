# IoTSIM

To reduce the complexity of the code, we used lombok in the pom.xml which provides getter and setter for different variables automatically. Based on the version of the Eclipse, lombok version need to updated. If this does not work, please download the jar file from  https://projectlombok.org/downloads/lombok.jar and install using command line > java -jar lombok.jar. Finally restart the Eclipse to make the change effective.

The IoTSim-Edge codebase is dependent on CloudSim which is provided as external jar file located in EdgeIoTSim\lib folder. One has to change the location of CloudSim jar file to compile succesfully.

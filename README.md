# EasyTV Rule based Matchmaker 

## Installation instructions

First, get the code by running the following command in a terminal window:

    git clone https://github.com/sgannoum/easytv-rule-based-matchmaker-certh.git

Then change to the newly created directory and run the following command:

    mvn install
	
## Running the Rule based Matchmaker
  
You can use any of java application servers to deploy the generated .war file
		
## Docker image

You can build a docker image from the docker file contained in the statistical matchmaker by running the following command:

	docker build --tag rbmm:v1 .

## License

The code of the EasyTV rule based Matchmaker component is available under the [Apache 2.0 license](https://github.com/sgannoum/easytv-rule-based-matchmaker-analysis-certh/blob/master/LICENSE.txt).

## Funding Acknowledgement

The research leading to these results has received funding from
the European Union's H2020-ICT-2016-2, ICT-19-2017 Media and content convergence
under grant agreement no. 761999.
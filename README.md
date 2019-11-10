# SummerREU

This is the code I worked on for my Summer REU. 


![Pipeline Picture](pipeline.jpg?raw=true "Pipeline Pic")  

## Step 1: Go into examplePipeline    
        1a) The whole point of this part of the code is to extract the press releases that:   
            a) Contain the relevant keywords    
            b) Fall within the same date    
        1b) Go into the /tools.java and run the Date_filter method.    
            This method will go into the entire corpus of the press releases and move the ones that fall under the given date             constraints into a file called "Asset".       
        1c) Go into the ASSET_extractor and run the main method. (Add the keywords you want)     
        1d) Go into main.java and run the main method.   
            Use the entire press release corpus as training data for the TF-IDF     
## 2: Extract the relevant news articles and tweets.      
        2a) Go into tools and run the bingMain.py which gets all of the news articles for a given press release using the bing search api.      
        2b) https://groups.google.com/forum/#!topic/snap-datasets/4_sMYlFRf9Q Download the Stanford SNAP Twitter dataset and then run the indexer.py on it.      
        2c) Run oldtweetminer.py on your press releases.     

## Step 3: Extract features
        3) One of the features I used was sentiment. Use the vaderSentiment.py on your press releases to get sentiment. 
       



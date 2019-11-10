from __future__ import division
import glob
import nltk
from nltk.tokenize import sent_tokenize
from nltk.tokenize import RegexpTokenizer
import math
import linecache
import matplotlib.pyplot as plt
import matplotlib.pyplot as p


#path = '/home/john/Desktop/REU/title_and_urls/topPercentile/reports/*.txt'
path = '/home/john/Desktop/REU/finalassetreports/*.txt'

#sample_path = '/home/john/Desktop/REU/sample.txt'
success_axis = []
Flesh_axis = []
FOG_axis = []
SMOG_axis = []

def CountSyllables(word, isName=True):
    vowels = "aeiouy"
    #single syllables in words like bread and lead, but split in names like Breanne and Adreann
    specials = ["ia","ea"] if isName else ["ia"]
    specials_except_end = ["ie","ya","es","ed"]  #seperate syllables unless ending the word
    currentWord = word.lower()
    numVowels = 0
    lastWasVowel = False
    last_letter = ""

    for letter in currentWord:
        if letter in vowels:
            #don't count diphthongs unless special cases
            combo = last_letter+letter
            if lastWasVowel and combo not in specials and combo not in specials_except_end:
                lastWasVowel = True
            else:
                numVowels += 1
                lastWasVowel = True
        else:
            lastWasVowel = False

        last_letter = letter

    #remove es & ed which are usually silent
    if len(currentWord) > 2 and currentWord[-2:] in specials_except_end:
        numVowels -= 1

    #remove silent single e, but not ee since it counted it before and we should be correct
    elif len(currentWord) > 2 and currentWord[-1:] == "e" and currentWord[-2:] != "ee":
        numVowels -= 1

    return numVowels

def Flesch_score(fileName):
    tokenizer = RegexpTokenizer(r'\w+')
    total_syllables = 0
    with open(fileName, 'r') as f:
        data=f.read().replace('\n', '')
        sent_tokenize_list = sent_tokenize(data)
        tokenized_words = tokenizer.tokenize(data)
        while '\xc2' in tokenized_words: tokenized_words.remove('\xc2')
        word_count = (len(tokenized_words))

        sent_count = (len(sent_tokenize_list))
    for x in tokenizer.tokenize(data):
        total_syllables = total_syllables + CountSyllables(x, True)

    Flesch_reading_ease = 206.835-(1.015*(word_count/sent_count))-(84.6*(total_syllables/word_count))
    return Flesch_reading_ease

def grab_success(fileName):
    z = fileName.split('/')[6]

    ##go to the title_and_urls folder to grab relevant file
    ##go through each title file and look for the original name
    title_path = '/home/john/Desktop/REU/title_and_urls/*.txt'
    path_for_successNum = '/home/john/Desktop/REU/title_and_urls/'
    for x in glob.glob(title_path):
        title = linecache.getline(x, 1).strip('\n')
        if z == title:

            with open(x) as fp:

                for line in fp:


                    if line.startswith('-------'):

                        newsLine = fp.next()
                        tweetLine = fp.next()
                    	newsLine = int(newsLine[9:])
                        tweetLine = int(tweetLine[10:])
            	        success_rating = newsLine/24 + tweetLine/65
                        return success_rating

def FOG_score(Filename):
    tokenizer = RegexpTokenizer(r'\w+')
    complexWordCount = 0
    with open(fileName, 'r') as f:
        data=f.read().replace('\n', '')
        sent_tokenize_list = sent_tokenize(data)
        tokenized_words = tokenizer.tokenize(data)
        while '\xc2' in tokenized_words: tokenized_words.remove('\xc2')
        word_count = (len(tokenized_words))

        sent_count = (len(sent_tokenize_list))
    for x in tokenizer.tokenize(data):
        syl_count = CountSyllables(x, True)
        if syl_count > 2:
            complexWordCount = complexWordCount + 1
    x = ((word_count/sent_count) + (100*(complexWordCount/word_count)))
    FOG = x*.4
    return FOG

### Not going to use because not all documents have over 30 sents
def SMOG_score(fileName):
    tokenizer = RegexpTokenizer(r'\w+')
    polySyllables = 0
    with open(fileName, 'r') as f:
        data=f.read().replace('\n', '')
        sent_tokenize_list = sent_tokenize(data)
        tokenized_words = tokenizer.tokenize(data)
        while '\xc2' in tokenized_words: tokenized_words.remove('\xc2')
        word_count = (len(tokenized_words))

        sent_count = (len(sent_tokenize_list))
    for x in tokenizer.tokenize(data):
        syl_count = CountSyllables(x, True)
        if syl_count > 2:
            polySyllables = polySyllables + 1
    x = (polySyllables * (30/sent_count))

    SMOG = 1.0430 * math.sqrt(x) + 3.1291
    return SMOG

t = '/home/john/Desktop/REU/finalassetreports/aioa-nab101909.php.txt'

for fileName in glob.glob(t):
    #print("--------")
    #print(fileName)
    #print(Flesch_score(fileName))
    #print(SMOG_score(fileName)) NOT ACCURATE
    #print(FOG_score(fileName))
    #print(grab_success(fileName))
    print(FOG_score(fileName))
    '''
    success_axis.append(grab_success(fileName))
    Flesh_axis.append(Flesch_score(fileName))
    FOG_axis.append(FOG_score(fileName))
    '''
'''
plt.plot(FOG_axis, success_axis, 'ro')
plt.ylabel('Success Ratio ')
plt.xlabel('Flesch score')
plt.axis([0,100,0,1.5])## x, y
plt.show()
'''

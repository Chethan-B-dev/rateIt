from sklearn.metrics import confusion_matrix, classification_report
from sklearn.naive_bayes import MultinomialNB
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.model_selection import train_test_split
import pandas as pd
msg = pd.read_csv('data6.csv', names=['message', 'label'])
msg['labelnum'] = msg.label.map({'pos': 1, 'neg': 0})
X = msg.message
Y = msg.labelnum
print('\nThe message and its label of first 5 instances are listed below')
X5, Y5 = X[0:5], msg.label[0:5]
for x, y in zip(X5, Y5):
    print(x, ',', y)
xtrain, xtest, ytrain, ytest = train_test_split(X, Y, test_size=0.2)
print('\nDataset is split into Training and Testing samples')
print('Total training instances:', xtrain.shape[0])
print('Total testing instances:', xtest.shape[0])
count_vect = CountVectorizer()
xtrain_dtm = count_vect.fit_transform(xtrain)
xtest_dtm = count_vect.transform(xtest)
print('\nTotal features extracted using CountVectorizer:', xtrain_dtm.shape[1])
print('\nFeatures for first 5 training instances are listed below:')
df = pd.DataFrame(xtrain_dtm.toarray(), columns=count_vect.get_feature_names())
print(df[0:5])
print(xtrain_dtm)
clf = MultinomialNB().fit(xtrain_dtm, ytrain)
predicted = clf.predict(xtest_dtm)
print('Confusion matrix')
print(confusion_matrix(ytest, predicted))
print('Classification report')
print(classification_report(ytest, predicted))

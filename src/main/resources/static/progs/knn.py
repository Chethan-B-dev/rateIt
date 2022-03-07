from sklearn.model_selection import train_test_split
from sklearn.neighbors import KNeighborsClassifier
from sklearn import datasets
from sklearn.metrics import confusion_matrix, classification_report, accuracy_score

iris = datasets.load_iris()
print("iris data set loaded")

xtrain, xtest, ytrain, ytest = train_test_split(
    iris.data, iris.target, test_size=0.3)
print(f"size of training data and its label {xtrain.shape} and {ytrain.shape}")
print(f"size of testing data and its label {xtest.shape} and {ytest.shape}")
for i in range(len(iris.target_names)):
    print("Label", i, "-", str(iris.target_names[i]))

classifier = KNeighborsClassifier(n_neighbors=3)  # k = 3
classifier.fit(xtrain, ytrain)
ypred = classifier.predict(xtest)
for r in range(0, len(xtest)):
    print(f"sample = {xtest[r]}")
    print(f"actual value = {ytest[r]}")
    print(f"predicted label = {ypred[r]}")

print(confusion_matrix(ytest, ypred))
print(classification_report(ytest, ypred))
print(accuracy_score(ytest, ypred))

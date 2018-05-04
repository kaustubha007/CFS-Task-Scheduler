import os.path,subprocess
import matplotlib.pyplot as plt
from subprocess import STDOUT,PIPE
import random
import time
import ast
import numpy as np

def compileJava(java_file):
    si = subprocess.STARTUPINFO()
    si.dwFlags |= subprocess.STARTF_USESHOWWINDOW
    subprocess.check_call(['javac', java_file], startupinfo = si)

def executeJava(java_file, stdin):
    si = subprocess.STARTUPINFO()
    si.dwFlags |= subprocess.STARTF_USESHOWWINDOW
    java_class, ext = os.path.splitext(java_file)
    cmd = ['java', java_class]
    proc = subprocess.Popen(cmd, stdin = PIPE, stdout = PIPE, stderr = STDOUT, encoding = 'utf8', startupinfo = si)
    stdout,stderr = proc.communicate(stdin)
    dictOutput = ast.literal_eval(str(stdout).replace('\\r\\n', '').replace('\\n', '').replace('=', ': '))
    return dictOutput

def plotGraph(execTimeDataRBT, execTimeDataHeap, numOfProcs, inputType):

    fig, (ax1) = plt.subplots()
    ax1.set_xticks(np.arange(0, 10, 1.0))
    ymax = max(max(execTimeDataRBT), max(execTimeDataHeap))
    ymin = min(min(execTimeDataRBT), min(execTimeDataHeap))
    
    ax1.plot([1,2,3,4,5,6,7,8,9,10], execTimeDataRBT, label = 'Red Black Tree')
    ax1.plot([1,2,3,4,5,6,7,8,9,10], execTimeDataHeap, label = 'Min Heap (Priority Queue)')
    ax1.set_ylim(ymin - ymin / 2, ymax + ymin / 2)
    ax1.set_title('RBT vs Heap Execution Comparison (' + inputType + ' for all iterations)')
    ax1.set_xlabel('Iteration number', fontsize = 12)
    ax1.set_ylabel('Execution Time (ms)', fontsize = 12)
    ax1.legend(bbox_to_anchor = (0.15, 1), loc = 2, borderaxespad = 0., prop = {'size': 14})
    ax1.annotate('No. of processes: ' + str(numOfProcs), xy = (0.75, 0.9), xycoords = 'axes fraction', xytext = (0.75, 0.9), textcoords = 'axes fraction')
    
    mng = plt.get_current_fig_manager()
    mng.resize(*mng.window.maxsize())
    plt.show()
    
def generateInputFile(noOfInputs, quantumSlice, fileName):
    f = open(fileName, 'w')
    f.write(str(quantumSlice) + "\n")
    if noOfInputs < 50:
        decimalPnt = 2
        upperBound = 1
        mulFactArr = 100
        mulFactTotal = 100
    elif noOfInputs < 500:
        decimalPnt = 3
        upperBound = 0.5
        mulFactArr = 1000
        mulFactTotal = 1000
    elif noOfInputs <= 1000:
        decimalPnt = 3
        upperBound = 1
        mulFactArr = 1000
        mulFactTotal = 1000
    elif noOfInputs <= 5000:
        decimalPnt = 4
        upperBound = 0.5
        mulFactArr = 10000
        mulFactTotal = 10000
    else:
        decimalPnt = 4
        upperBound = 0.8
        mulFactArr = 10000
        mulFactTotal = 10000
    for iProc in range(0, noOfInputs):
        f.write(str(iProc) + " " + str(int(float('{0:.{1}f}'.format(random.uniform(0, upperBound), decimalPnt)) * mulFactArr)) + " " + str(int(float('{0:.{1}f}'.format(random.uniform(0, upperBound), decimalPnt)) * mulFactTotal)) + "\n")
    f.close()

    
if "__main__" == __name__:

    choice = int(input(("1. Use manual input file\n2. Use system generated input file\n3. Use default input (Ip size = 10000, Slice = 75, Snapshot time = 0)\n4. Run test cases with different ip sizes\nPlease enter your choice: ")))
    if choice == 1:
        fileName = str(input("Make sure the input file is in the same directory as this code. Enter file name with extension: "))
        snapshotTimeUnit = int(input("Enter the time unit for RB Tree snapshot: "))
    elif choice == 3:
        fileName = 'inputCFS.txt'
        noOfInputs = 1000
        quantumSlice = 40
        snapshotTimeUnit = 0
        generateInputFile(noOfInputs, quantumSlice, fileName)
    elif choice == 4:
        dictTestCase = {'T1': [5, 4], 'T2': [10, 8], 'T3': [50, 12], 'T4': [100, 16], 'T5': [500, 20], 'T6': [1000, 40]}
##        ,
##                             'T7': [5000, 60], 'T8': [10000, 80], 'T9': [50000, 100], 'T10': [100000, 160], 'T11': [500000, 200], 'T12': [1000000, 320]}
        listAllRBTExecTime = []
        listAllHeapExecTime = []
        listRBTHeapDiffMax = []
        inputTypes = ['Same Input', 'Different Input']
        directories = ['TestRuns/Inputs/', 'TestRuns/Graphs/' + inputTypes[0], 'TestRuns/Graphs/' + inputTypes[1]]
        for directory in directories:
            if not os.path.exists(directory):
                os.makedirs(directory)
        for inputType in inputTypes:
            for testCase, val in dictTestCase.items():
                print(testCase)
                print(val)

                noOfInputs = val[0]
                quantumSlice = val[1]
                fileName = directories[0] + 'inputCFS' + testCase + '.txt'
                snapshotTimeUnit = 0
                inputsToSend = fileName + "\n" + str(snapshotTimeUnit)
                if inputType == 'Same Input':
                    generateInputFile(noOfInputs, quantumSlice, fileName)
                listRBTExecTime = []
                listHeapExecTime = []
                listRBTHeapDiff = []
                for iLoop in range(10):
                    if inputType == 'Different Input':
                        generateInputFile(noOfInputs, quantumSlice, fileName)
                    dictOutput = executeJava('FileReaderCFS.java', inputsToSend)
                    listRBTExecTime.append(float(dictOutput['RBTExecutionTime']))
                    listHeapExecTime.append(float(dictOutput['HeapExecutionTime']))
                    listRBTHeapDiff.append(float(dictOutput['RBTExecutionTime']) - float(dictOutput['HeapExecutionTime']))
                indexMax = listRBTHeapDiff.index(max(listRBTHeapDiff))
                tempList = [listRBTExecTime[indexMax], listHeapExecTime[indexMax]]
                listRBTHeapDiffMax.append(tempList)
                print("RBT: " + str(listRBTExecTime))
                print("Heap:" + str(listHeapExecTime))
                print(listRBTHeapDiffMax)
                plotGraph(listRBTExecTime, listHeapExecTime, val[0], inputType)
            
    else:
        fileName = 'inputCFS.txt'
        noOfInputs = int(input("Enter no. of inputs: "))
        quantumSlice = int(input("Enter quantum slice: "))
        snapshotTimeUnit = int(input("Enter the time unit for RB Tree snapshot: "))
        generateInputFile(noOfInputs, quantumSlice, fileName)
    inputsToSend = fileName + "\n" + str(snapshotTimeUnit)
    start = time.time()
    compileJava('FileReaderCFS.java')
    dictOutput = executeJava('FileReaderCFS.java', inputsToSend)
    end = time.time()
    
        
    for node in dictOutput['RBTreeSnapShot']:
        print(node)
    print(dictOutput)
    print("Red Black Tree execution time: " + dictOutput['RBTExecutionTime'] + " ms")
    print("Heap execution time: " + dictOutput['HeapExecutionTime'] + " ms")
    print("Total run time = %.5f s" %(end - start))

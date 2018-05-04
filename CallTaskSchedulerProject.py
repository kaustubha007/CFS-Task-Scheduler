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

def plotGraph(xAxisData, execTimeDataRBT, execTimeDataHeap, directoryPath, title, xLabel, numOfProcs = None, interval = 1.0):
    fig, (ax1) = plt.subplots()
    ax1.set_xticks(np.arange(min(xAxisData), max(xAxisData) + 1, interval))
    ymax = max(max(execTimeDataRBT), max(execTimeDataHeap))
    ymin = min(min(execTimeDataRBT), min(execTimeDataHeap))
    
    ax1.plot(xAxisData, execTimeDataRBT, label = 'Red Black Tree')
    ax1.plot(xAxisData, execTimeDataHeap, label = 'Min Heap (Priority Queue)')
    ax1.set_ylim(ymin - ymin / 2, ymax + ymin / 2)
    ax1.set_title(title)
    ax1.set_xlabel(xLabel, fontsize = 12)
    ax1.set_ylabel('Execution Time (ms)', fontsize = 12)
    ax1.legend(bbox_to_anchor = (0.15, 1), loc = 2, borderaxespad = 0., prop = {'size': 14})
    if numOfProcs != None:
        ax1.annotate('No. of processes: ' + str(numOfProcs), xy = (0.75, 0.9), xycoords = 'axes fraction', xytext = (0.75, 0.9), textcoords = 'axes fraction')
    
    mng = plt.get_current_fig_manager()
    mng.resize(*mng.window.maxsize())
    
    plt.savefig(directoryPath + '.png', dpi = 'figure')
##    plt.show()
    
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


def runTestCases():
##        dictTestCase = {'T1': [5, 4], 'T2': [10, 8], 'T3': [50, 12], 'T4': [100, 16], 'T5': [500, 20], 'T6': [1000, 40],
##                             'T7': [5000, 60], 'T8': [10000, 80], 'T9': [50000, 100], 'T10': [100000, 160], 'T11': [500000, 200], 'T12': [1000000, 320]}
    dictTestCase = {'T1': [1000, 40], 'T2': [5000, 60], 'T3': [10000, 80], 'T4': [50000, 100]}
    inputTypes = ['Same Input', 'Different Input']
    directories = ['TestRuns/Inputs/', 'TestRuns/Graphs/']

    for inputType in inputTypes:
        listAllRBTExecTime = []
        listAllHeapExecTime = []
        listRBTHeapDiffMax = []
        maxRBTList = []
        maxHeapList = []
        for testCase, val in dictTestCase.items():
            print("Running test case " + testCase + " with " + inputType + " for all the 10 iterations.")
            print("No. of inputs: " + str(val[0]))
            print("Quantum Slice: " + str(val[1]))

            noOfInputs = val[0]
            quantumSlice = val[1]
            snapshotTimeUnit = 0
            if inputType == 'Same Input':
                dirInp = directories[0] + inputType + '/'
                if not os.path.exists(dirInp):
                    os.makedirs(dirInp)
                fileName = dirInp + 'inputCFS' + testCase + '.txt'
                directory = directories[1] + inputType + '/'
                if not os.path.exists(directory):
                    os.makedirs(directory)
                generateInputFile(noOfInputs, quantumSlice, fileName)
            else:
                directory = directories[1] + inputType + '/'
                if not os.path.exists(directory):
                    os.makedirs(directory)
                
            listRBTExecTime = []
            listHeapExecTime = []
            listRBTHeapDiff = []
            
            for iLoop in range(10):
                
                if inputType == 'Different Input':
                    dirInp = directories[0] + inputType + '/' + testCase + '/'
                    if not os.path.exists(dirInp):
                        os.makedirs(dirInp)
                    fileName = dirInp + 'inputCFS' + testCase + str(iLoop + 1) + '.txt'
                    generateInputFile(noOfInputs, quantumSlice, fileName)
                inputsToSend = fileName + "\n" + str(snapshotTimeUnit)
                dictOutput = executeJava('FileReaderCFS.java', inputsToSend)
                listRBTExecTime.append(float(dictOutput['RBTExecutionTime']))
                listHeapExecTime.append(float(dictOutput['HeapExecutionTime']))
                listRBTHeapDiff.append(float(dictOutput['RBTExecutionTime']) - float(dictOutput['HeapExecutionTime']))
            indexMax = listRBTHeapDiff.index(max(listRBTHeapDiff))
            maxRBTList.append(listRBTExecTime[indexMax])
            maxHeapList.append(listHeapExecTime[indexMax])
            plotGraph([1, 2, 3, 4, 5, 6, 7, 8, 9, 10], listRBTExecTime, listHeapExecTime, directory + testCase,
                      'RBT vs Heap Execution Comparison (' + inputType + ' for all iterations)', 'Iteration number', val[0])
        directory = directories[1] + '/' + inputType + ' All test cases'
        
        plotGraph([v[0] for k,v in dictTestCase.items()], maxRBTList, maxHeapList, directory,
                        'RBT vs Heap Execution comparison for all test cases', 'No. of processes', None, 7000)

        print("All the input files and graphs have been stored in the TestRuns folder.")

    
if "__main__" == __name__:
    compileJava('FileReaderCFS.java')
    while(True):
        choice = int(input(("1. Use manual input file\n2. Use system generated input file\n3. Use default input (Ip size = 10000, Quantum Slice = 75, Snapshot time = 0)\n4. Run test cases with different ip sizes\nPlease enter your choice (Enter 0 to exit): ")))
        if choice == 1:
            fileName = str(input("Make sure the input file is in the same directory as this code. Enter file name with extension: "))
            snapshotTimeUnit = int(input("Enter the time unit for RB Tree snapshot: "))
        elif choice == 2:
            fileName = 'inputCFS.txt'
            noOfInputs = int(input("Enter no. of inputs (To get a snapshot and order of execution, no. of inputs should be less than or equal to 50): "))
            quantumSlice = int(input("Enter quantum slice: "))
            if(noOfInputs <= 50):
                snapshotTimeUnit = int(input("Enter the time unit for RB Tree snapshot: "))
            else:
                snapshotTimeUnit = 0
            generateInputFile(noOfInputs, quantumSlice, fileName)
        elif choice == 3:
            fileName = 'inputCFS.txt'
            noOfInputs = 10000
            quantumSlice = 75
            snapshotTimeUnit = 0
            generateInputFile(noOfInputs, quantumSlice, fileName)
        elif choice == 4:
            start = time.time()
            runTestCases()
            end = time.time()
            print("\nTotal run time for the entire program = %.5f s" %(end - start))
        else:
            break
        

        if choice != 4:
            inputsToSend = fileName + "\n" + str(snapshotTimeUnit)
            
            start = time.time()
            dictOutput = executeJava('FileReaderCFS.java', inputsToSend)
            end = time.time()
            if snapshotTimeUnit != 0:
                print("A snapshot of red black tree at time unit " + str(snapshotTimeUnit))
                for node in dictOutput['RBTreeSnapShot']:
                    print(node)
                print("\nOrder of processes executed by the processor:")
                print("Red Black Tree")
                print(dictOutput['RBTExecutedProcesses'])
                print("Min Heap")
                print(dictOutput['HeapExecutedProcesses'])
            
            print("\nRed Black Tree execution time: " + dictOutput['RBTExecutionTime'] + " ms")
            print("Heap execution time: " + dictOutput['HeapExecutionTime'] + " ms")
            print("\nTotal run time for the entire program = %.5f s" %(end - start))

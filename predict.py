#!/usr/bin/env python3
import pandas as pd
import numpy as np
from sklearn.preprocessing import MinMaxScaler
from sklearn.metrics import mean_squared_error
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense
from tensorflow.keras.layers import Flatten
from tensorflow.keras.layers import Dropout
from tensorflow.keras.layers import Activation
from tensorflow import keras
from tensorflow.keras.layers import LSTM
import tensorflow
import keras
from tensorflow.keras import optimizers
from tensorflow.keras.callbacks import EarlyStopping

import io

import tensorflow.keras.backend as K

def accuracy_80(y_true, y_pred):
    mask_p = K.greater(y_pred, 0.8)  # element-wise True where y_pred > 0.8
    mask_p = K.cast(mask_p, K.floatx())  # cast to 0.0 / 1.0
    mask_t = K.greater(y_true, 0.8)  # element-wise True where y_true > 0.8
    mask_t = K.cast(mask_t, K.floatx())  # cast to 0.0 / 1.0
    mask_n = K.not_equal(mask_t, mask_p)
    mask_n = K.cast(mask_n, K.floatx())  # cast to 0.0 / 1.0
    mask = K.equal(mask_t, mask_p)
    mask = K.cast(mask, K.floatx())  # cast to 0.0 / 1.0
    acc= K.sum(mask)/( K.sum(mask)+ K.sum(mask_n))
    return acc

def recall_80(y_true, y_pred):
    mask_p = K.greater(y_pred, 0.8)          
    mask_p = K.cast(mask_p, K.floatx())       # mask_p has 1 where y_pred>0.8 and 0 elsewhere
    mask_t = K.greater(y_true, 0.8)       
    mask_t = K.cast(mask_t, K.floatx())       # same for mask_t and y_true
    FN = K.less(mask_p, mask_t)          
    FN = K.cast(FN, K.floatx())               # Our FN results are where mask_p is less than mask_t => predicted 0 but result was 1
    mask_o = K.less_equal(y_true, 0.8)
    mask_o = K.cast(mask_o, K.floatx())       # Calculate the opposite tensor of mask_t, it's 1 where y_true<0.8
    TP = K.greater(mask_p, mask_o)            # Our TP results are the elements where mask_p>mask_o,
    TP = K.cast(TP, K.floatx())               # because that means mask_p=1, we predicted 1, and mask_o=0, the result was 1
    nom = K.maximum(K.sum(TP),0.0000000001)
    den = K.maximum(K.sum(TP)+ K.sum(FN),0.0000000001)   
    rec = tf.divide(nom, den)                 # recall = TP/(TP+FN)
    return rec


    
from numpy import newaxis
import numpy as np
from tensorflow.keras.models import load_model

dependencies = {
    'accuracy_80': accuracy_80,
    'recall_80': recall_80
}
prefix='erg/'

model=load_model(prefix+'model_no_vms_in_use.h5', custom_objects=dependencies)
  
zero_t=(prefix+'vm0.csv')
one_t=(prefix+'vm1.csv')
two_t=(prefix+'vm2.csv')
three_t=(prefix+'vm3.csv')
four_t=(prefix+'vm4.csv')
five_t=(prefix+'vm5.csv')
six_t=(prefix+'vm6.csv')
seven_t=(prefix+'vm7.csv')
eight_t=(prefix+'vm8.csv')
nine_t=(prefix+'vm9.csv')
ten_t=(prefix+'vm10.csv')
eleven_t=(prefix+'vm11.csv')
twelve_t=(prefix+'vm12.csv')
thirteen_t=(prefix+'vm13.csv')
fourteen_t=(prefix+'vm14.csv')
fifteen_t=(prefix+'vm15.csv')
sixteen_t=(prefix+'vm16.csv')
seventeen_t=(prefix+'vm17.csv')
eighteen_t=(prefix+'vm18.csv')
nineteen_t=(prefix+'vm19.csv')

state_file=(prefix+'dataset.csv')


a= np.loadtxt(zero_t, delimiter=',')

b= np.loadtxt(one_t, delimiter=',')

c= np.loadtxt(two_t, delimiter=',')

d= np.loadtxt(three_t, delimiter=',')

e= np.loadtxt(four_t, delimiter=',')

f= np.loadtxt(five_t, delimiter=',')

g= np.loadtxt(six_t, delimiter=',')

h= np.loadtxt(seven_t, delimiter=',')

i= np.loadtxt(eight_t, delimiter=',')

j= np.loadtxt(nine_t, delimiter=',')

k= np.loadtxt(ten_t, delimiter=',')

l= np.loadtxt(eleven_t, delimiter=',')

m= np.loadtxt(twelve_t, delimiter=',')

n= np.loadtxt(thirteen_t, delimiter=',')

o= np.loadtxt(fourteen_t, delimiter=',')

p= np.loadtxt(fifteen_t, delimiter=',')

q= np.loadtxt(sixteen_t, delimiter=',')

r= np.loadtxt(seventeen_t, delimiter=',')

s= np.loadtxt(eighteen_t, delimiter=',')

t= np.loadtxt(nineteen_t, delimiter=',')

   
state= np.loadtxt(state_file, delimiter=',')

scale=[1, 604800, 100, 100, 100, 55, 20]
scale_Y = [100]

a_t=np.divide(a, scale)
a_t = np.delete(a_t, [0,6], 1)
a_t = a_t[newaxis,:,:]

b_t=np.divide(b, scale)
b_t = np.delete(b_t, [0,6], 1)
b_t = b_t[newaxis,:,:]

c_t=np.divide(c, scale)
c_t = np.delete(c_t, [0,6], 1)
c_t = c_t[newaxis,:,:]

d_t=np.divide(d, scale)
d_t = np.delete(d_t, [0,6], 1)
d_t = d_t[newaxis,:,:]

e_t=np.divide(e, scale)
e_t = np.delete(e_t, [0,6], 1)
e_t = e_t[newaxis,:,:]

f_t=np.divide(f, scale)
f_t = np.delete(f_t, [0,6], 1)
f_t = f_t[newaxis,:,:]

g_t=np.divide(g, scale)
g_t = np.delete(g_t, [0,6], 1)
g_t = g_t[newaxis,:,:]

h_t=np.divide(h, scale)
h_t = np.delete(h_t, [0,6], 1)
h_t = h_t[newaxis,:,:]

i_t=np.divide(i, scale)
i_t = np.delete(i_t, [0,6], 1)
i_t = i_t[newaxis,:,:]

j_t=np.divide(j, scale)
j_t = np.delete(j_t, [0,6], 1)
j_t = j_t[newaxis,:,:]

k_t=np.divide(k, scale)
k_t = np.delete(k_t, [0,6], 1)
k_t = k_t[newaxis,:,:]

l_t=np.divide(l, scale)
l_t = np.delete(l_t, [0,6], 1)
l_t = l_t[newaxis,:,:]

m_t=np.divide(m, scale)
m_t = np.delete(m_t, [0,6], 1)
m_t = m_t[newaxis,:,:]

n_t=np.divide(n, scale)
n_t = np.delete(n_t, [0,6], 1)
n_t = n_t[newaxis,:,:]

o_t=np.divide(o, scale)
o_t = np.delete(o_t, [0,6], 1)
o_t = o_t[newaxis,:,:]

p_t=np.divide(p, scale)
p_t = np.delete(p_t, [0,6], 1)
p_t = p_t[newaxis,:,:]

q_t=np.divide(q, scale)
q_t = np.delete(q_t, [0,6], 1)
q_t = q_t[newaxis,:,:]

r_t=np.divide(r, scale)
r_t = np.delete(r_t, [0,6], 1)
r_t = r_t[newaxis,:,:]

s_t=np.divide(s, scale)
s_t = np.delete(s_t, [0,6], 1)
s_t = s_t[newaxis,:,:]

t_t=np.divide(t, scale)
t_t = np.delete(t_t, [0,6], 1)
t_t = t_t[newaxis,:,:]

state=np.divide(state, scale)
                                                                            
day = state[0][1]//(60*60*24)
time = state[0][1]%(60*60*24)
#vms_in_use=state[0][6]
b=np.delete(state, [0,1,6], 1).flatten()
#b = np.insert(b, 0, vms_in_use, axis=0)
b = np.insert(b, 0, time, axis=0)
b = np.insert(b, 0, day, axis=0)
b=b[newaxis,:]


alist=[]
input = [a_t,b]
prediction = np.multiply(model.predict(input), scale_Y)[0][0]
alist.append(prediction)

input = [b_t,b]
prediction = np.multiply(model.predict(input), scale_Y)[0][0]
alist.append(prediction)

input = [c_t,b]
prediction = np.multiply(model.predict(input), scale_Y)[0][0]
alist.append(prediction)

input = [d_t,b]
prediction = np.multiply(model.predict(input), scale_Y)[0][0]
alist.append(prediction)

input = [e_t,b]
prediction = np.multiply(model.predict(input), scale_Y)[0][0]
alist.append(prediction)

input = [f_t,b]
prediction = np.multiply(model.predict(input), scale_Y)[0][0]
alist.append(prediction)

input = [g_t,b]
prediction = np.multiply(model.predict(input), scale_Y)[0][0]
alist.append(prediction)

input = [h_t,b]
prediction = np.multiply(model.predict(input), scale_Y)[0][0]
alist.append(prediction)

input = [i_t,b]
prediction = np.multiply(model.predict(input), scale_Y)[0][0]
alist.append(prediction)

input = [j_t,b]
prediction = np.multiply(model.predict(input), scale_Y)[0][0]
alist.append(prediction)

input = [k_t,b]
prediction = np.multiply(model.predict(input), scale_Y)[0][0]
alist.append(prediction)

input = [l_t,b]
prediction = np.multiply(model.predict(input), scale_Y)[0][0]
alist.append(prediction)

input = [m_t,b]
prediction = np.multiply(model.predict(input), scale_Y)[0][0]
alist.append(prediction)

input = [n_t,b]
prediction = np.multiply(model.predict(input), scale_Y)[0][0]
alist.append(prediction)

input = [o_t,b]
prediction = np.multiply(model.predict(input), scale_Y)[0][0]
alist.append(prediction)

input = [p_t,b]
prediction = np.multiply(model.predict(input), scale_Y)[0][0]
alist.append(prediction)

input = [q_t,b]
prediction = np.multiply(model.predict(input), scale_Y)[0][0]
alist.append(prediction)

input = [r_t,b]
prediction = np.multiply(model.predict(input), scale_Y)[0][0]
alist.append(prediction)

input = [s_t,b]
prediction = np.multiply(model.predict(input), scale_Y)[0][0]
alist.append(prediction)

input = [t_t,b]
prediction = np.multiply(model.predict(input), scale_Y)[0][0]
alist.append(prediction)

#arr = np.array(alist)
arr=np.around(alist, decimals=2)
np.savetxt(prefix+'prediction.csv', [arr], delimiter=",")
print(arr)

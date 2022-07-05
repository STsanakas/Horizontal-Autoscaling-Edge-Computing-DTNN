# Intelligent Horizontal Autoscaling in Edge Computing using a Double Tower Neural Network

The code expects a trainX list which consists of the numpy RNN input (samples, timesteps, features) and the numpy Feedforward input (samples, features), in that order. Same goes for testX. trainY and testY should contain a single output (samples, output)
home_folder is used to save the best model, the models' results and the state at the start of each iteration as a checkpoint so it can resume instead of needing to restart in case the run gets interrupted midway.

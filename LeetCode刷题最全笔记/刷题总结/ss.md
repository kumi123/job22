Q值为过程噪声，越小系统越容易收敛，我们对模型预测的值信任度越高；但是太小则容易发散，如果Q为零，那么我们只相信预测值；Q值越大我们对于预测的信任度就越低，而对测量值的信任度就变高；如果Q值无穷大，那么我们只信任测量值；R值为测量噪声，太小太大都不一定合适。R太大，卡尔曼滤波响应会变慢，因为它对新测量的值的信任度降低；越小系统收敛越快，但过小则容易出现震荡；测试时可以保持陀螺仪不动，记录一段时间内陀螺仪的输出数据，这个数据近似正态分布，按3σ原则，取正态分布的(3σ)^2作为R的初始化值。

测试时可以先将Q从小往大调整，将R从大往小调整；先固定一个值去调整另外一个值，看收敛速度与波形输出。

系统中还有一个关键值P，它是误差协方差初始值，表示我们对当前预测状态的信任度，它越小说明我们越相信当前预测状态；它的值决定了初始收敛速度，一般开始设一个较小的值以便于获取较快的收敛速度。随着卡尔曼滤波的迭代，P的值会不断的改变，当系统进入稳态之后P值会收敛成一个最小的估计方差矩阵，这个时候的卡尔曼增益也是最优的，所以这个值只是影响初始收敛速度。
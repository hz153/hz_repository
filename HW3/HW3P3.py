def max_steal(v):
    """
    :param v: 每家的价值数组
    :return: 最大偷取的价值
    """
    # steal从第一家到这一家最大偷取的价值
    steal = [0, v[0]]
    for j in range(2, len(v) + 1):
        # 小偷要么选择当前这一家，那么总收益是当前这一家+前j-2家的最大偷取价值
        # 要么就是不选择这一家，那么总收益就是前面这一家的最大偷取价值
        if v[j - 1] + steal[j - 2] > steal[j - 1]:
            steal.append(v[j - 1] + steal[j - 2])
        else:
            steal.append(steal[j - 1])
    return steal[len(v)]


if __name__ == '__main__':
    v1 = [5, 3, 4, 11, 2]
    print(max_steal(v1))

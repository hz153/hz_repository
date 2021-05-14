def paring(n):
    """
    :param p: 配对的总数
    :param n: 剩余配对的人数
    :return: 配对的总数
    """
    if n < 0:
        return 0
    if n == 0:
        return 1
    return paring(n-1) + (n-1)*paring(n-2)


if __name__ == '__main__':
    friend_num = 4
    print(paring(friend_num))

import random


def exchange(list1, pos1, pos2):
    """
    交换数组的内容
    :param list1: 传入数组
    :param pos1: 交换位置1
    :param pos2: 交换位置2
    :return: 交换好的数组
    """
    temp = list1[pos1]
    list1[pos1] = list1[pos2]
    list1[pos2] = temp
    return list1


def partition(A, p, r):
    """
    :param A: 待排序的数组
    :param p: 起始位置
    :param r: 结束位置
    :return: 划分索引p和t
    """
    s = random.randint(p, r)
    # exchange A[r] and A[s]
    exchange(A, r, s)
    pivot = A[r]
    q = p-1
    t = p-1
    for j in range(p, r):
        if A[j] < pivot:
            if q == t:
                exchange(A, j, t+1)
            else:
                exchange(A, j, t+1)
                exchange(A, t+1, q+1)
            q += 1
            t += 1
        elif A[j] == pivot:
            exchange(A, j, t+1)
            t += 1
    exchange(A, t+1, r)
    q += 1
    t += 1
    return q, t


def randomized_quicksort(A, p, r):
    """
    带重复项的快速排序
    :param A: 待排序的数组
    :param p: 起始位置
    :param r: 结束位置
    :return: 排好序的数组
    """
    if p < r:
        q, t = partition(A, p, r)
        randomized_quicksort(A, p, q-1)
        randomized_quicksort(A, t+1, r)
    return A


if __name__ == '__main__':
    # 测试数组
    A = [0, 2, 1, 4, 3, 2, 6, 1, 1, 2]
    p = 0
    r = len(A)-1
    print(randomized_quicksort(A, p, r))


# [leetcode]ä¸å¥æ³æ³ðå·ænä¸ªéåæ çé®é¢

> æ¬æå°å¸¦ä½ ç¨æ çä¸ç§éåç®æ³è§£å³Nä¸ª`leetcode`ç¸å³ç®æ³é¢(ç®æ³å°æ¸£æ¸£è´æ¬å¶å¸å)
>
> ![img](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/171a13141ad2ce15)

> æä¸å®³ææ¾ç¶ç·´éä¸è¬ç¨®è¸¢æ³çäººï¼ä½æå®³æä¸ç¨®è¸¢æ³ç·´éä¸è¬æ¬¡çäºº(by å¶å¸åçå¾å¼Bruce Lee)
>
> ![img](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/171a13141ad2ce15)

# æ çéå(Traversal)

å¦ä¸å¾, ä¸ç§éåæ¹å¼, å¯ç¨åä¸ç§éå½ææ³å®ç°

![img](data:image/svg+xml;utf8,<?xml version="1.0"?><svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="1229" height="699"></svg>)



## ååºéå(PreOrder, æç§åè®¿é®æ ¹èç¹çé¡ºåº)

```
var preorderTraversal = function(root) {
  const res = []
  function traversal (root) {
    if (root !== null) {
      res.push(root.val) // è®¿é®æ ¹èç¹çå¼
      traversal(root.left) // éå½éåå·¦å­æ 
      traversal(root.right) // éå½éåå³å­æ 
    }
  }
  traversal(root)
  return res
}
å¤å¶ä»£ç 
```

## 94 ä¸­åºéå(InOrder, æç§æ ¹èç¹å¨ä¸­é´è®¿é®çé¡ºåº)



![img](data:image/svg+xml;utf8,<?xml version="1.0"?><svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="1280" height="898"></svg>)



```
var inorderTraversal = function(root) {
  const res = []
  function traversal (root) {
    if (root !== null) {
      traversal(root.left)
      res.push(root.val)
      traversal(root.right)
    }
  }
  traversal(root)
  return res
}
å¤å¶ä»£ç 
```

## 145 åç»­éå(PosterOrder, æç§æ ¹èç¹å¨åé¢è®¿é®çé¡ºåº)



![img](data:image/svg+xml;utf8,<?xml version="1.0"?><svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="1280" height="890"></svg>)



```
var postorderTraversal = function(root) {
  const res = []
  function traversal (root) {
    if (root !== null) {
      traversal(root.left)
      traversal(root.right)
      res.push(root.val)
    }
  }
  traversal(root)
  return res
}
å¤å¶ä»£ç 
```

## 100 ç¸åçæ 



![img](data:image/svg+xml;utf8,<?xml version="1.0"?><svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="1280" height="1223"></svg>)



å¯ä»¥å©ç¨è¿ç§éå½ææ³å¹¶ååæ¶ç¬ä¸¤æ£µæ 

```
var isSameTree = function(p, q) {
  function traversal (root1, root2) {
    if (root1 === null && root2 !== null) {
      return false
    } else if (root1 !== null && root2 === null) {
      return false
    } else if (root1 === null && root2 === null) {
      return true
    } else {
      return  root1.val === root2.val && traversal(root1.left, root2.left) && traversal(root1.right, root2.right)
    }
  }
  return traversal(p, q)
}
å¤å¶ä»£ç 
```

## 226 ç¿»è½¬äºåæ 



![img](data:image/svg+xml;utf8,<?xml version="1.0"?><svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="1270" height="1280"></svg>)



è¿ç§ç®æ³å¯ä»¥å¸®å©`Homebrew`ä½è`Max Howell`è§£å¼`Google`çç®æ³é¢è¯é¢



![img](data:image/svg+xml;utf8,<?xml version="1.0"?><svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="655" height="373"></svg>)



```
var invertTree = function(root) {
  function traversal (root) {
    if (root === null) {
      return null
    } else {
      [root.left, root.right] = [traversal(root.right), traversal(root.left)]
      return root
    }
  }
  return  traversal(root)
}
å¤å¶ä»£ç 
```

## 590 Nåæ çååºéå



![img](data:image/svg+xml;utf8,<?xml version="1.0"?><svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="1280" height="1156"></svg>)



æä»¬è¿å¯ä»¥ç¨æ­¤ç§ç®æ³è§£å³Nåæ çé®é¢

```
var postorder = function(root) {
  const res = []
  function traversal (root) {
    if (root !== null) {
      root.children.forEach(child => {
        traversal(child)
      })
      res.push(root.val)
    }
  }
  traversal(root)
  return res
}
å¤å¶ä»£ç 
```

å¦æä½ å·²å¯¹è¿ç§åæ³å®¡ç¾ç²å³, å¯ä»¥æ¢ä¸ªåæ³, ä½¿ç¨å¿åå½æ°

```
var postorder = function(root) {
  const res = []
  ;(function (root) {
    if (root !== null) {
      root.children.forEach(child => {
        arguments.callee(child)
      })
      res.push(root.val)
    }
  })(root)
  return res
}
å¤å¶ä»£ç 
```

è¿å¯ä»¥å©ç¨æ æ¥è¿­ä»£

```
var postorder = function(root) {
  if (root === null) {
    return []
  }
  const res = []
  const arr = [root]
  while (arr.length) {
    const cur = arr.pop()
    res.push(cur.val)
    for (let i = cur.children.length - 1; i >= 0; i--) {
      arr.push(cur.children[i])
    }
  }
  return res.reverse()
}
å¤å¶ä»£ç 
```

## 103 äºåæ çé¯é½¿å½¢å±æ¬¡éå



![img](data:image/svg+xml;utf8,<?xml version="1.0"?><svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="1280" height="1111"></svg>)



å¤§ç½è¯, èç®èµ°ä½ç¬æ 

```
var zigzagLevelOrder = function(root) {
  if (root === null) {
    return []
  } else {
    let res = []
    function traversal (root, depth) {
      if (root !== null) {
        if (res[depth] === undefined) {
          res[depth] = []
        }
        res[depth].push(root.val)
        traversal(root.left, depth + 1)
        traversal(root.right, depth + 1)
      }
    }
    traversal(root, 0)
    res.forEach((item, index) => {
      if (index & 1) {
        res[index] = item.reverse()
      }
    })
    return res
  }
}
å¤å¶ä»£ç 
```

ä¼å

```
var zigzagLevelOrder = function(root) {
  if (root === null) {
    return []
  } else {
    let res = []
    function traversal (root, depth) {
      if (root !== null) {
        if (res[depth] === undefined) {
          res[depth] = []
        }
        if (depth & 1) {
          res[depth].unshift(root.val)
        } else {
          res[depth].push(root.val)
        }
        traversal(root.left, depth + 1)
        traversal(root.right, depth + 1)
      }
    }
    traversal(root, 0)
    return res
  }
}
å¤å¶ä»£ç 
```

## 230 äºåæç´¢æ ä¸­ç¬¬Kå°çåç´ 



![img](data:image/svg+xml;utf8,<?xml version="1.0"?><svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="1160" height="1280"></svg>)



```
var kthSmallest = function (root, k) {
  let arr = []
  function traversal (node) {
    if (node !== null) {
      traversal(node.left)
      arr.push(node.val)
      traversal(node.right)
    }
  }
  traversal(root)
  return arr[k - 1]
}
å¤å¶ä»£ç 
```

ä¼å, åå°éåæ¬¡æ°

```
var kthSmallest = function (root, k) {
  let arr = []
  function traversal(node) {
    if (node !== null && arr.length < k) {
      traversal(node.left)
      arr.push(node.val)
      traversal(node.right)
    }
  }
  traversal(root)
  return arr[k - 1]
}
å¤å¶ä»£ç 
```

è¿ä¸æ­¥ä¼å, ä½¿ç¨O(1)çé¢å¤ç©ºé´

```
var kthSmallest = function (root, k) {
  let res
  let count = 0
  function traversal(node) {
    if (node !== null) {
      if (count < k) {
        traversal(node.left)
      }
      if (++count === k) {
        res = node.val
      }
      if (count < k) {
        traversal(node.right)
      }
    }
  }
  traversal(root)
  return res
}
å¤å¶ä»£ç 
```

## 102 äºåæ çå±åºéå



![img](data:image/svg+xml;utf8,<?xml version="1.0"?><svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="1280" height="1136"></svg>)



```
var levelOrder = function(root) {
  const res = []
  function traversal (root, depth) {
    if (root !== null) {
      if (!res[depth]) {
        res[depth] = []
      }
      traversal(root.left, depth + 1)
      res[depth].push(root.val)
      traversal(root.right, depth + 1)
    }
  }
  traversal(root, 0)
  return res
}
å¤å¶ä»£ç 
```

## 199 äºåæ çå³è§å¾



![img](data:image/svg+xml;utf8,<?xml version="1.0"?><svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="1280" height="884"></svg>)



åºæ¬æè·¯: ååºéå, è®°å½æ¯ä¸å±æ·±åº¦ä¸çèç¹çå¼, å¹¶åè®°å½å·¦èç¹åè®°å½å³èç¹, åæåè®°å½çå¼å³ä¸ºè¯¥å±æ·±åº¦çå³è§å¾çå°çå¼

```
var rightSideView = function(root) {
  const arr = []
  function traversal (root, depth) {
    if (root) {
      if (arr[depth] === undefined) {
        arr[depth] = []
      }
      arr[depth].push(root.val)
      traversal(root.left, depth + 1)
      traversal(root.right, depth + 1)
    }
  }
  traversal(root, 0)
  const res = []
  for (let i = 0; i < arr.length; ++i) {
    res.push(arr[i][arr[i].length - 1])
  }
  return res
};
å¤å¶ä»£ç 
```

## 104 äºåæ çæå¤§æ·±åº¦



![img](data:image/svg+xml;utf8,<?xml version="1.0"?><svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="1280" height="927"></svg>)



```
var maxDepth = function (root) {
  let res = 0
  function traversal (root, depth) {
    if (root !== null) {
      if (depth > res) {
        res = depth
      }
      if (root.left) {
        traversal(root.left, depth + 1)
      }
      if (root.right) {
        traversal(root.right, depth + 1)
      }
    }
  }
  traversal(root, 1)
  return res
}
å¤å¶ä»£ç 
```

## 107 äºåæ çå±æ¬¡éå II



![img](data:image/svg+xml;utf8,<?xml version="1.0"?><svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="1280" height="1107"></svg>)



```
var levelOrderBottom = function(root) {
  if (root === null) {
    return []
  }
  let res = []
  function traversal (root, depth) {
    if (root !== null) {
      if (!res[depth]) {
        res[depth] = []
      }
      traversal(root.left, depth + 1)
      res[depth].push(root.val)
      traversal(root.right, depth + 1)
    }
  }
  traversal(root, 0)
  return res.reverse()
}
å¤å¶ä»£ç 
```

## 671 äºåæ ä¸­ç¬¬äºå°çèç¹



![img](data:image/svg+xml;utf8,<?xml version="1.0"?><svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="1188" height="1280"></svg>)



```
var findSecondMinimumValue = function(root) {
  let arr = []
  ;(function traversal (root) {
    if (root !== null) {
      traversal(root.left)
      arr.push(root.val)
      traversal(root.right)
    }
  })(root)
  let _arr = [...new Set(arr)].sort()
  return _arr[1] ? _arr[1] : -1
}
å¤å¶ä»£ç 
```

## 1038 ä»äºåæç´¢æ å°æ´å¤§åæ 



![img](data:image/svg+xml;utf8,<?xml version="1.0"?><svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="1276" height="1280"></svg>)



```
var bstToGst = function(root) {
  let sum = 0
  function traversal (root) {
    if (root !== null) {
      traversal(root.right)
      root.val += sum
      sum = root.val
      traversal(root.left)
    }
  }
  traversal(root)
  return root
}
å¤å¶ä»£ç 
```

## 538 æäºåæç´¢æ è½¬æ¢ä¸ºç´¯å æ 



![img](data:image/svg+xml;utf8,<?xml version="1.0"?><svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="800" height="600"></svg>)



```
var convertBST = function(root) {
  let sum = 0
  function traversal (root) {
    if (root !== null) {
      traversal(root.right)
      sum += root.val
      root.val = sum
      traversal(root.left)
    }
  }
  traversal(root)
  return root
}
å¤å¶ä»£ç 
```

## 700 äºåæç´¢æ ä¸­çæç´¢



![img](data:image/svg+xml;utf8,<?xml version="1.0"?><svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="1280" height="1221"></svg>)



```
var searchBST = function(root, val) {
  function traversal (root) {
    if (root !== null) {
      if (root.val === val) {
        return root
      } else if (root.val < val) {
        return traversal(root.right)
      } else {
        return traversal(root.left)
      }
    } else {
      return root
    }
  }
  return traversal(root)
}
å¤å¶ä»£ç 
```

## 559 Nåæ çæå¤§æ·±åº¦



![img](data:image/svg+xml;utf8,<?xml version="1.0"?><svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="1280" height="1274"></svg>)



```
var maxDepth = function(root) {
  if (root === null) {
    return 0
  } else {
    let depth = 1
    function traversal (root, curDepth) {
      if (root !== null) {
        if (curDepth > depth) {
          depth = curDepth
        }
        root.children.forEach(child => traversal(child, curDepth + 1))
      }
    }
    traversal(root, 1)
    return depth
  }
}
å¤å¶ä»£ç 
```

## 589 Nåæ çååºéå



![img](data:image/svg+xml;utf8,<?xml version="1.0"?><svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="1280" height="1175"></svg>)



```
var preorder = function(root) {
  const res = []
  function traversal (root) {
    if (root !== null) {
      res.push(root.val)
      root.children.forEach(child => traversal(child))
    }
  }
  traversal(root)
  return res
}
å¤å¶ä»£ç 
```

## 897 éå¢é¡ºåºæ¥æ¾æ 



![img](https://cdn.jsdelivr.net/gh/kumi123/CDN//img2/171a13141ad2ce15)



```
var increasingBST = function(root) {
  const arr = []
  function traversal (root) {
    if (root !== null) {
      traversal(root.left)
      arr.push(root.val)
      traversal(root.right)
    }
  }
  traversal(root)
  const res = new TreeNode(arr[0])
  let currentNode = res
  for (let i = 0; i < arr.length - 1; i++) {
    currentNode.left = null
    currentNode.right = new TreeNode(arr[i + 1])
    currentNode = currentNode.right
  }
  return res
}
å¤å¶ä»£ç 
```

åæå¨æé: [juejin.im/post/5e1c4eâ¦](https://juejin.im/post/5e1c4e46f265da3e140fa54d)
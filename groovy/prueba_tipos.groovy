a = [1,2,3]
b = [1,2,6]

byte[] z = [100, 100]
byte[] pq = [23, 24]

a.addAll(b)
println a
n = z.toList()
o = pq.toList()
println n
n.addAll(pq)
println(n)

n.add(77)
println n
n.addAll([55,55])
println(n)

def array_bytes = n.toArray(Byte)
println(array_bytes)
z

reop = new List(S)
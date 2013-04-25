def objectToJson(Obj):
  if hasattr(Obj, "toJson"):
    return Obj.toJson()
  else:
    raise TypeError, "Could not turn object into JSON"



class Jsonable:
  def toJson(self):
    noFunctions = dict((k, v) for k, v in self.__dict__.items() if not hasattr(v, '__call__'))
    return noFunctions
    
class Referable(Jsonable):
  def getRef(self):
    return self.refFn(self.id)
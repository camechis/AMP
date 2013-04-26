


class Where(object):
  
  def __init__(self, prop):
    self.property = prop
  
  def Contains(self, value):
    return lambda obj: obj[self.property].find(value) is not -1
    
  def NotContains(self, value):
    return lambda obj: obj[self.property].find(value) is -1
  
  def StartsWith(self, value):
    return lambda obj: obj.startswith(value)
  
  def EndsWith(self, value):
    return lambda obj: obj.endswith(value)
  
  def Is(self, value):
    return lambda obj: obj[self.property] == value
  
  def IsNot(self, value):
    return lambda obj: obj[self.property] != value
  
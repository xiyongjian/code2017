'''

@author: xyj
'''


class Employee:
   'Common base class for all employees'
   empCount = 0

   def __init__(self, name, salary):
      self.name = name
      self.salary = salary
      Employee.empCount += 1
   
   def displayCount(self):
     print ("Total Employee %d" % Employee.empCount)

   def displayEmployee(self):
      print ("Name : ", self.name, ", Salary: ", self.salary)
      
   def __str__(self):
      return 'Employee (%d)' % (self.empCount)


def use_obj():
    print ("\n-- use_obj() --")
    a = 40  # Create object <40>
    b = a  # Increase ref. count  of <40> 
    c = [b]  # Increase ref. count  of <40> 
    print("a's type : ", type(a))
    print("a's type .__dict__ : ", type(a).__dict__);
    print("a's type .__dict__ : ", type(a).__dict__);
    print("id of a, b, c", id(a), id(b), id(c))


class Point:

   def __init__(self, x=0, y=0):
      self.x = x
      self.y = y

   def __del__(self):
      class_name = self.__class__.__name__
      print (class_name, "destroyed")


def use_point():
    print ("\n-- use_point() --")
    pt1 = Point()
    pt2 = pt1
    pt3 = pt1
    print (id(pt1), id(pt2), id(pt3));  # prints the ids of the obejcts
    del pt1
    del pt2
    del pt3

    
class Parent:  # define parent class
   parentAttr = 100

   def __init__(self):
      print ("Calling parent constructor")

   def parentMethod(self):
      print ('Calling parent method')

   def setAttr(self, attr):
      Parent.parentAttr = attr

   def getAttr(self):
      print ("Parent attribute :", Parent.parentAttr)


class Child(Parent):  # define child class

   def __init__(self):
      print ("Calling child constructor")

   def childMethod(self):
      print ('Calling child method')


def use_inheritence():
    print ("\n-- use_inheritence() --")
    print ("Parent.__doc__:", Parent.__doc__)
    print ("Parent.__name__:", Parent.__name__)
    print ("Parent.__module__:", Parent.__module__)
    print ("Parent.__bases__:", Parent.__bases__)
    print ("Parent.__dict__:", str(Parent.__dict__).replace(",", ",\n"))
    print ("Child.__doc__:", Child.__doc__)
    print ("Child.__name__:", Child.__name__)
    print ("Child.__module__:", Child.__module__)
    print ("Child.__bases__:", Child.__bases__)
    print ("Child.__dict__:", str(Child.__dict__).replace(",", ",\n"))
    
def use_emp():
    print ("\n-- use_emp() --")
    # This would create first object of Employee class"
    emp1 = Employee("Zara", 2000)
    # This would create second object of Employee class"
    emp2 = Employee("Manni", 5000)
    emp1.displayEmployee()
    emp2.displayEmployee()
    print ("Total Employee %d" % Employee.empCount)
    print ("repr emp1 : ", repr(emp1));
    print ("str emp1 : ", str(emp1));
    
    print ("Employee.__doc__:", Employee.__doc__)
    print ("Employee.__name__:", Employee.__name__)
    print ("Employee.__module__:", Employee.__module__)
    print ("Employee.__bases__:", Employee.__bases__)
    print ("Employee.__dict__:", str(Employee.__dict__).replace(",", ",\n"))

def use_object():
    print ("\n-- use_object() --")
    print ("object.__doc__:", object.__doc__)
    print ("object.__name__:", object.__name__)
    print ("object.__module__:", object.__module__)
    print ("object.__bases__:", object.__bases__)
    print ("object.__dict__:", str(object.__dict__).replace(",", ",\n"))
    
    print("type of object.__dict__:", type(object.__dict__))
    
def use_attr():
    print ("\n-- use_attr() --")
    emp1 = Employee("Zara", 2000)
    print ("emp1.__dict__:", str(emp1.__dict__).replace(",", ",\n"))
    print ("dir(emp1) :", str(dir(emp1)).replace(",", ",\n"))
    print ("dir(Employee) :", str(dir(Employee)).replace(",", ",\n"))
    print ("class travesal");
    print (emp1.__class__);
    print (emp1.__class__.__base__);
    print("mro(method resolution order) : ", Employee.mro());
    
#     obj = object();
#     print ("obj.__dict__:", str(obj.__dict__).replace(",", ",\n"))
#     print ("dir(obj) :", str(dir(obj)).replace(",", ",\n"))

if __name__ == '__main__':
#     use_emp()
#     use_obj()
#     use_point()
#     use_inheritence()
#     use_object()
    use_attr()
    


#!/usr/bin/ruby

file = File.open("test.properties")

properties = ""

file.each { |line|
  properties << "-D" 
  properties << line
  properties << " "
}

puts "Executing Tests"
puts "-------------------------------------------------"
puts "Providing Test Properties:"
puts properties
puts "-------------------------------------------------"
puts ""

exec(%{mvn test -DargLine="#{properties}"})
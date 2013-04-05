#!/usr/bin/ruby

fileToOpen = ARGV[0] ?  ARGV[0] : "test.properties"

puts "Using #{fileToOpen}"

file = File.open(fileToOpen)

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
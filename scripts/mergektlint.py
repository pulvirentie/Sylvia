import os.path
from xml.etree import ElementTree

first = None
file_list = [
    "sylvia/build/reports/ktlint/ktlintMainSourceSetCheck.xml",
    "sylvia/build/reports/ktlint/ktlintTestSourceSetCheck.xml"
]

ktlintFile = 'build/reports/ktlint/ktlint-report.xml'

for filename in file_list:
    if os.path.isfile(filename):
        data = ElementTree.parse(filename).getroot()
        if first is None:
            first = data
        else:
            first.extend(data)
if first is not None:
    f = open(ktlintFile, 'w')
    f.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n")
    f.write(ElementTree.tostring(first))
    f.close()

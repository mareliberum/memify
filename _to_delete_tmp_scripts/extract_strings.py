import sys
import xml.etree.ElementTree as ET

# usage: extract_strings.py <app_strings_xml> <output_xml> name1 name2 ...
app_strings_path = sys.argv[1]
out_path = sys.argv[2]
names = sys.argv[3:]

tree = ET.parse(app_strings_path)
root = tree.getroot()

found = {}
for el in root.findall("string"):
    n = el.get("name")
    if n in names:
        found[n] = el

missing = [n for n in names if n not in found]
if missing:
    print(f"MISSING: {missing}")
    sys.exit(1)

out_root = ET.Element("resources")
for n in names:
    el = found[n]
    new_el = ET.SubElement(out_root, "string", {"name": n})
    new_el.text = el.text
out_tree = ET.ElementTree(out_root)
ET.indent(out_tree, space="    ")
out_tree.write(out_path, encoding="utf-8", xml_declaration=False)
# prepend newline for readability, ensure trailing newline
with open(out_path, "r", encoding="utf-8") as f:
    content = f.read()
with open(out_path, "w", encoding="utf-8") as f:
    f.write(content + "\n")
print(f"OK: wrote {len(names)} strings to {out_path}")

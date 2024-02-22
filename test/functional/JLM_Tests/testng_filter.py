import sys

pattern_start = f'<test name="{sys.argv[1]}">'
pattern_end = '</test>'
skip = False

with open(sys.argv[2], 'r') as file:
    for line in file:
        if (pattern_start in line):
            skip = True
        if (not skip):
            sys.stdout.write(line)
        if (pattern_end in line):
            skip = False

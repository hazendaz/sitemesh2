#!perl
#
# sitemesh2 (https://github.com/hazendaz/sitemesh2)
#
# Copyright 2011-2025 Hazendaz.
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of The Apache Software License,
# Version 2.0 which accompanies this distribution, and is available at
# https://www.apache.org/licenses/LICENSE-2.0.txt
#
# Contributors:
#     Hazendaz (Jeremy Landis).
#


$n = int (rand 1000) + 1;
$hour = (localtime)[2];

if ($hour > 3 && $hour < 12) {
	$t = "morning";
}
elsif ($hour < 19) {
	$t = "afternoon";
}
elsif ($hour < 21) {
	$t = "evening";
}
else {
	$t = "night";
}

print <<"EOF";
Content-type: text/html

<html>
	<head>
		<title>Random number $n</title>
	</head>
	<body>

		The full power of Perl is being harnessed to generate the number of
		<b>$n!</b>

		<p>

		Have a nice $t.

	</body>
</html>
EOF


create materialized view incommon.incommon as
select
	xmltable.*
from incommon.incommon_raw,
xmltable(
	'//IDPSSODescriptor/Extensions/Scope' passing raw
	columns
		Organization text path './text()',
		DisplayName text path '..//UIInfo/DisplayName[@xml:lang="en"]/text()',
		Logo text path '..//UIInfo/Logo[1]'
	)
;

create index icorg on incommon.incommon(organization);